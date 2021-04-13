package com.makeopinion.cpxresearchlib

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.makeopinion.cpxresearchlib.misc.isEqualTo
import com.makeopinion.cpxresearchlib.models.*
import com.makeopinion.cpxresearchlib.views.CPXBannerViewHandler
import com.makeopinion.cpxresearchlib.views.CPXBannerViewHandlerListener
import com.makeopinion.cpxresearchlib.views.CPXWebActivityListener
import com.makeopinion.cpxresearchlib.views.CPXWebViewActivity

class CPXResearch private constructor(
    private val configuration: CPXConfiguration
) {
    companion object {
        /**
         * Initialise the CPXResearch Library. Must be called before any other functions.
         *
         * @param context The application context.
         * @param configuration The configuration object
         * @return
         */
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

    /**
     * Stops the automatic request for new surveys.
     */
    fun deactivateAutomaticCheckForSurveys() {
        intervalHandler?.removeCallbacks(onIntervalSurveysCheck)
        removeBanner()
        intervalHandler = null
    }

    /**
     * Starts the automatic request for new surveys. If already active this function does nothing.
     */
    fun activateAutomaticCheckForSurveys() {
        if (intervalHandler == null) {
            intervalHandler = Handler(Looper.getMainLooper())
            intervalHandler?.post(onIntervalSurveysCheck)
        }
    }

    /**
     * Allow or disallow showing a survey banner/notification in general.
     *
     * @param isVisible If set to *true* a banner/notification is shown, if there are surveys available, otherwise the banner/notification is never visible.
     * @param onActivity The activity the banner will be shown on.
     */
    fun setSurveyVisibleIfAvailable(isVisible: Boolean, onActivity: Activity) {
        showBannerIfSurveysAreAvailable = isVisible
        if (isVisible) {
            requestSurveyUpdate(true)
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

    /**
     * Updates the banner/notification that is shown in case of available surveys.
     *
     * @param newStyle The new style information for the changed banner.
     */
    fun setStyle(newStyle: CPXStyleConfiguration) {
        configuration.style = newStyle
        installBanner()
    }

    /**
     * Request a update on available surveys (and unpaid transactions).
     * New surveys and/or unpaid transactions will be published through the *CPXResearchListener*.
     *
     * @param includeUnpaidTransactions Set to true if a current list of unpaid transactions should be requested as well.
     */
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

    /**
     * Show the current available surveys in a web view in a new activity.
     *
     * @param onActivity The activity that starts the new activity.
     */
    fun openSurveyList(onActivity: Activity) {
        CPXWebViewActivity.launchSurveysActivity(onActivity,
            configuration,
            webActivityEvenHandler)
    }

    /**
     * Shows the given survey in a web view in a new activity.
     *
     * @param onActivity The activity that starts the new activity.
     * @param byId The survey id of the survey to show.
     */
    fun openSurvey(onActivity: Activity, byId: String) {
        CPXWebViewActivity.launchSingleSurveyActivity(onActivity,
        configuration,
        byId)
    }

    /**
     * Shows the dialog in which the user can select for how long no surveys should be shown.
     *
     * @param onActivity The activity that starts the new activity.
     */
    fun openHideSurveysDialog(onActivity: Activity) {
        CPXWebViewActivity.launchHideDialogActivity(onActivity, configuration)
    }

    /**
     * Tells CPX Research that a transaction has been paid to the user. This removes the transaction from the unpaid list.
     * If the transaction id/message id is invalid this function does not throw any error.
     *
     * @param transactionId The transaction id of the transaction that has been paid.
     * @param messageId The message id of the transaction that has been paid.
     */
    fun markTransactionAsPaid(transactionId: String, messageId: String) {
        val queryItems = HashMap<String, String?>()
        queryItems["transaction_mode"] = "full"
        queryItems["transaction_set_paid"] = "true"
        queryItems["cpx_message_id"] = messageId

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

    /**
     * Adds a listener of CPXResearch events
     *
     * @param listener The listener object to add. If this listener is already known this function will do nothing.
     */
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
            response.surveys?.let { surveyArray ->
                val newSurveys = surveyArray.toList()
                if (!newSurveys.isEqualTo(surveys)) {
                    surveys = surveyArray.toMutableList()
                    listeners.forEach { it.onSurveysUpdated() }
                }

                response.transactions?.let { transactionArray ->
                    val newTransactions = transactionArray.toList()
                    if (!newTransactions.isEqualTo(unpaidTransactions)) {
                        unpaidTransactions = transactionArray.toMutableList()
                        listeners.forEach { it.onTransactionsUpdated(unpaidTransactions) }
                    }
                }

                cpxText = response.text
                installBanner()
            }
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