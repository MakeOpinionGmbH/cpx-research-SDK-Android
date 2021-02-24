package com.makeopinion.cpxresearchdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.models.TransactionItem


class TransactionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onResume() {
        super.onResume()
        (activity?.application as? CPXApplication)?.let {
            (recyclerView.adapter as TransactionAdapter).setData(it.cpxResearch().unpaidTransactions)
            it.cpxResearch().registerListener(object : CPXResearchListener {
                override fun onSurveysUpdated() { }

                override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) {
                    (recyclerView.adapter as TransactionAdapter).setData(it.cpxResearch().unpaidTransactions)
                }

                override fun onSurveysDidOpen() { }

                override fun onSurveysDidClose() { }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        (activity?.application as? CPXApplication)?.let { app ->
            val adapter = TransactionAdapter(app.cpxResearch().unpaidTransactions)
            recyclerView.adapter = adapter
        }
        return view
    }
}