package com.makeopinion.cpxresearchlib.models

import java.io.Serializable

class CPXWebViewConfiguration @JvmOverloads constructor(
    /**
     * If true a help button might be shown in WebViews depending on context.
     */
    val allowHelpButton: Boolean = true,
    /**
     * If true a settings button might be shown in WebViews depending on context.
     */
    val allowSettingsButton: Boolean = true,
    /**
     * If true a home button might be shown in WebViews depending on context.
     */
    val allowHomeButton: Boolean = true,
    /**
     * Select when to show an alert when the user tries to exit the webview.
     */
    val showCloseConfirmationDialog: ShowConfirmationDialog = ShowConfirmationDialog.SURVEY_AND_SURVEYLIST
) : Serializable {
    enum class ShowConfirmationDialog {
        /**
         * Show alert when a specific survey or the survey list was initially opened.
         */
        SURVEY_AND_SURVEYLIST,
        /**
         * Show alert when a specific survey was initially opened.
         */
        SURVEY_ONLY,
        /**
         * Never show any confirmation dialog when the user tries to close the webview.
         */
        NEVER
    }
}