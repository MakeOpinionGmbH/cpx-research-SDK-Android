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


class SurveyFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onResume() {
        super.onResume()

        (activity?.application as? CPXApplication)?.let {
            (recyclerView.adapter as SurveyAdapter).setData(it.cpxResearch().surveys)
            it.cpxResearch().registerListener(object : CPXResearchListener {
                override fun onSurveysUpdated() {
                    (recyclerView.adapter as SurveyAdapter).setData(it.cpxResearch().surveys)
                }

                override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) { }

                override fun onSurveysDidOpen() { }

                override fun onSurveysDidClose() { }

                override fun onSurveyDidOpen() { }

                override fun onSurveyDidClose() { }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        (activity?.application as? CPXApplication)?.let { app ->
            val adapter = SurveyAdapter(app.cpxResearch().surveys) { view ->
                view?.let { app.cpxResearch().openSurvey(requireActivity(), it.tag as String) }
            }
            recyclerView.adapter = adapter
        }

        return view
    }
}