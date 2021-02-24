package com.makeopinion.cpxresearchlib

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.makeopinion.cpxresearchlib.misc.CPXHash
import com.makeopinion.cpxresearchlib.models.*
import com.makeopinion.cpxresearchlib.views.CPXBannerViewHandler
import com.makeopinion.cpxresearchlib.views.CPXBannerViewHandlerListener
import com.makeopinion.cpxresearchlib.views.CPXWebActivityListener
import com.makeopinion.cpxresearchlib.views.CPXWebViewActivity

class CPXResearch private constructor(
    private val configuration: CPXConfiguration
) {
    companion object {
        fun init(context: Context, configuration: CPXConfiguration): CPXResearch {
            AndroidNetworking.initialize(context)
            return CPXResearch(configuration)
        }
    }

    private val api = NetworkService()
    private var listeners = emptyList<CPXResearchListener>()
    private var bannerViewHandler: CPXBannerViewHandler? = null
    private var intervalHandler: Handler? = null

    var surveys = emptyList<SurveyItem>().toMutableList()
        private set
    var unpaidTransactions = emptyList<TransactionItem>().toMutableList()
        private set
    var cpxText: SurveyTextItem? = null
        private set
    var showBannerIfSurveysAreAvailable = false
        private set

    fun deactivateAutomaticCheckForSurveys() {
        intervalHandler?.removeCallbacks(onIntervalSurveysCheck)
        removeBanner()
        intervalHandler = null
    }

    fun activateAutomaticCheckForSurveys() {
        if (intervalHandler == null) {
            intervalHandler = Handler(Looper.getMainLooper())
            intervalHandler?.post(onIntervalSurveysCheck)
        }
    }

    fun setSurveyVisibleIfAvailable(isVisible: Boolean, onActivity: Activity) {
        showBannerIfSurveysAreAvailable = isVisible
        if (isVisible) {
            bannerViewHandler = CPXBannerViewHandler(onActivity,
                configuration,
                object : CPXBannerViewHandlerListener {
                    override fun onImageTapped() {
                        openSurveyList(onActivity)
                    }
                })
        }
        installBanner()
    }

    fun setStyle(newStyle: CPXStyleConfiguration) {
        configuration.style = newStyle
        installBanner()
    }

    fun requestSurveyUpdate(includeUnpaidTransactions: Boolean = false) {
        val queryItems = HashMap<String, String?>()
        if (includeUnpaidTransactions)
            queryItems["show_unpaid_transactions"] = "1"

        api.requestSurveysFromApi(
            configuration,
            queryItems,
            onSurveyUpdateHandler
        )
    }

    fun openSurveyList(onActivity: Activity) {
        CPXWebViewActivity.launchSurveysActivity(onActivity,
            configuration,
            webActivityEvenHandler)
    }

    fun openSurvey(onActivity: Activity, byId: String) {
        CPXWebViewActivity.launchSingleSurveyActivity(onActivity,
        configuration,
        byId)
    }

    fun openHideSurveysDialog(onActivity: Activity) {
        CPXWebViewActivity.launchHideDialogActivity(onActivity, configuration)
    }

    fun markTransactionAsPaid(transactionId: String, messageId: String) {
        val queryItems = HashMap<String, String?>()
        queryItems["transaction_mode"] = "full"
        queryItems["transaction_set_paid"] = "true"
        queryItems["cpx_message_id"] = messageId
        queryItems["secure_hash"] =
            CPXHash.md5("${configuration.extUserId}-${configuration.secureKey}")

        api.requestSurveysFromApi(
            configuration,
            queryItems,
            onSurveyUpdateHandler
        )

        unpaidTransactions
            .find { it.transId == transactionId }
            ?.let {
                unpaidTransactions.remove(it)
                listeners.forEach { listener ->
                    listener.onTransactionsUpdated(unpaidTransactions)
                }
            }
    }

    fun registerListener(listener: CPXResearchListener) {
        if (!listeners.contains(listener)) {
            listeners += listener
        }
    }

    // Internal
    private fun installBanner() {
        removeBanner()
        if (surveys.size > 0 && showBannerIfSurveysAreAvailable)
            bannerViewHandler?.install()
    }

    private fun removeBanner() {
        bannerViewHandler?.remove()
    }

    private val onSurveyUpdateHandler = object : ResponseListener {
        override fun onSurveyResponse(response: SurveyModel) {
            surveys = response.surveys.toMutableList()
            listeners.forEach { it.onSurveysUpdated() }

            if (response.transactions.isNotEmpty()) {
                unpaidTransactions = response.transactions.toMutableList()
                listeners.forEach { it.onTransactionsUpdated(unpaidTransactions) }
            }

            cpxText = response.text
            installBanner()
        }

        override fun onError(anError: ANError) {
            Log.e("CPXResearchLib", "Error", anError)
        }
    }

    private val webActivityEvenHandler = object : CPXWebActivityListener {
        override fun onDidOpen() {
            listeners.forEach {
                it.onSurveysDidOpen()
            }
        }

        override fun onDidClose() {
            listeners.forEach {
                it.onSurveysDidClose()
            }
        }
    }

    private val onIntervalSurveysCheck = object : Runnable {
        override fun run() {
            requestSurveyUpdate()
            intervalHandler?.postDelayed(this, 60000)
        }
    }
}