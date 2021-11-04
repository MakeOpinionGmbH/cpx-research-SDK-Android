package com.makeopinion.cpxresearchlib

import com.makeopinion.cpxresearchlib.models.TransactionItem

interface CPXResearchListener {
    /**
     * Called when the surveys did change, meaning there could be new, updated and/or removed ones.
     */
    fun onSurveysUpdated()

    /**
     * Called when there are changes (added, removed, edited) unpaid transactions.
     *
     * @param unpaidTransactions A list of currently unpaid transactions.
     */
    fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>)

    /**
     * The survey list was opened by a tap on a banner.
     */
    fun onSurveysDidOpen()

    /**
     * The survey list previously opened by a tap on a banner has been closed.
     */
    fun onSurveysDidClose()

    /**
     * A single survey was opened either by a direct call or by a click on a CPX Card
     */
    fun onSurveyDidOpen()

    /**
     * A single survey webview has been closed.
     */
    fun onSurveyDidClose()
}