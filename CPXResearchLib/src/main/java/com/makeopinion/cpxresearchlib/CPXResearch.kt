package com.makeopinion.cpxresearchlib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.makeopinion.cpxresearchlib.misc.isEqualTo
import com.makeopinion.cpxresearchlib.models.*
import com.makeopinion.cpxresearchlib.views.*

class CPXResearch(private val configuration: CPXConfiguration) {
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
            val cpx = CPXResearch(configuration)
            cpx.activateAutomaticCheckForSurveys()
            return cpx
        }
    }

    private val api = NetworkService()
    private var listeners = emptyList<CPXResearchListener>().toMutableList()
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
        CPXLogger.f("deactivateAutomaticCheckForSurveys")
        intervalHandler?.removeCallbacks(onIntervalSurveysCheck)
        intervalHandler = null
    }

    /**
     * Starts the automatic request for new surveys. If already active this function does nothing.
     */
    fun activateAutomaticCheckForSurveys() {
        CPXLogger.f("activateAutomaticCheckForSurveys")
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
        CPXLogger.f("setSurveyVisibleIfAvailable($isVisible, $onActivity)")
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

    /**
     * Updates the banner/notification that is shown in case of available surveys.
     *
     * @param newStyle The new style information for the changed banner.
     */
    fun setStyle(newStyle: CPXStyleConfiguration) {
        CPXLogger.f("setStyle($newStyle)")
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
        CPXLogger.f("requestSurveyUpdate($includeUnpaidTransactions)")
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
        CPXLogger.f("openSurveyList($onActivity)")
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
        CPXLogger.f("openSurvey($onActivity, $byId)")
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
        CPXLogger.f("openHideSurveysDialog($onActivity)")
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
        CPXLogger.f("markTransactionAsPaid($transactionId, $messageId)")
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
     * Tells CPX Research that a transaction has been paid to the user. This removes the transaction from the unpaid list.
     * If the transaction id/message id is invalid this function does not throw any error.
     * This function does call {@link #markTransactionAsPaid(String, String)} internally.
     *
     * @param transactionItem The transaction item to mark as paid.
     */
    fun markTransactionAsPaid(transactionItem: TransactionItem) {
        CPXLogger.f("markTransactionAsPaid($transactionItem)")
        markTransactionAsPaid(transactionItem.transId, transactionItem.messageId)
    }

    /**
     * Adds a listener of CPXResearch events
     *
     * @param listener The listener object to add. If this listener is already known this function will do nothing.
     */
    fun registerListener(listener: CPXResearchListener) {
        CPXLogger.f("registerListener($listener)")
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    /**
     * Activated/Deactivates the debug log mode. Logs are kept only in memory.
     *
     * @param isActive Set to true to enable debug logging, false otherwise.
     */
    fun setLogMode(isActive: Boolean) {
        CPXLogger.f("setLogMode($isActive)")
        CPXLogger.l("Switching logger to $isActive")
        CPXLogger.setEnabled(isActive)
        CPXLogger.l("Switched logger to $isActive")
    }

    /**
     * Opens the system dialog to choose a file to export the current log to.
     *
     * @param onActivity The activity to open the dialog on.
     */
    fun exportLog(onActivity: Activity) {
        CPXLogger.f("exportLog($onActivity)")
        val intent = Intent(onActivity, ExportLogActivity::class.java)
        onActivity.startActivity(intent)
    }

    // Internal
    private fun installBanner() {
        removeBanner()
        CPXLogger.f("installBanner()")
        if (surveys.size > 0 && showBannerIfSurveysAreAvailable)
            bannerViewHandler?.install()
    }

    private fun removeBanner() {
        CPXLogger.f("removeBanner()")
        bannerViewHandler?.remove()
    }

    private val onSurveyUpdateHandler = object : ResponseListener {
        override fun onSurveyResponse(model: SurveyModel) {
            CPXLogger.f("onSurveyResponse($model)")
            model.surveys?.let { surveyArray ->
                val newSurveys = surveyArray.toList()
                if (!newSurveys.isEqualTo(surveys)) {
                    surveys = surveyArray.toMutableList()
                    listeners.forEach { it.onSurveysUpdated() }
                }

                model.transactions?.let { transactionArray ->
                    val newTransactions = transactionArray.toList()
                    if (!newTransactions.isEqualTo(unpaidTransactions)) {
                        unpaidTransactions = transactionArray.toMutableList()
                        listeners.forEach { it.onTransactionsUpdated(unpaidTransactions) }
                    }
                }

                cpxText = model.text
                installBanner()
            }
        }

        override fun onError(anError: ANError) {
            CPXLogger.f("onError($anError)")
            Log.e("CPXResearchLib", "Error", anError)
        }
    }

    private val webActivityEvenHandler = object : CPXWebActivityListener {
        override fun onDidOpen() {
            CPXLogger.f("onDidOpen()")
            listeners.forEach {
                it.onSurveysDidOpen()
                CPXLogger.l("Listener $it called with onSurveysDidOpen()")
            }
        }

        override fun onDidClose() {
            CPXLogger.f("onDidClose()")
            listeners.forEach {
                it.onSurveysDidClose()
                CPXLogger.l("Listener $it called with onSurveysDidClose()")
            }
        }
    }

    private val onIntervalSurveysCheck = object : Runnable {
        override fun run() {
            CPXLogger.l("Scheduled requestSurveyUpdate() call")
            requestSurveyUpdate()
            intervalHandler?.postDelayed(this, 60000)
        }
    }
}