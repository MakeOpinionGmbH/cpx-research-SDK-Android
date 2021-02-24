package com.makeopinion.cpxresearchlib

import com.makeopinion.cpxresearchlib.models.TransactionItem

interface CPXResearchListener {
    fun onSurveysUpdated()
    fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>)

    fun onSurveysDidOpen()
    fun onSurveysDidClose()
}