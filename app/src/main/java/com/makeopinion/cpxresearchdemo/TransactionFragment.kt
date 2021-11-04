package com.makeopinion.cpxresearchdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.models.TransactionItem


class TransactionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var subheader: TextView

    override fun onResume() {
        super.onResume()
        (activity?.application as? CPXApplication)?.let {
            (recyclerView.adapter as TransactionAdapter).setData(it.cpxResearch().unpaidTransactions)
            subheader.text = "Tap on a transaction to mark it as paid.\nTotal transactions unpaid: ${it.cpxResearch().unpaidTransactions.size}"
            it.cpxResearch().registerListener(object : CPXResearchListener {
                override fun onSurveysUpdated() {}

                override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) {
                    (recyclerView.adapter as TransactionAdapter).setData(it.cpxResearch().unpaidTransactions)
                    subheader.text = "Tap on a transaction to mark it as paid.\nTotal transactions unpaid: ${unpaidTransactions.size}"
                }

                override fun onSurveysDidOpen() {}

                override fun onSurveysDidClose() {}

                override fun onSurveyDidOpen() { }

                override fun onSurveyDidClose() { }
            })
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        subheader = view.findViewById(R.id.tv_title)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        (activity?.application as? CPXApplication)?.let { app ->
            val adapter = TransactionAdapter(app.cpxResearch().unpaidTransactions) { view ->
                view?.let { foundView ->
                    val transactionId = foundView.tag as String
                    app.cpxResearch().unpaidTransactions.find { item ->
                        item.transId == transactionId
                    }?.let {
                        app.cpxResearch().markTransactionAsPaid(it.transId, it.messageId)
                    }
                }
            }
            recyclerView.adapter = adapter
        }
        return view
    }
}