package com.makeopinion.cpxresearchdemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.models.TransactionItem


class MainActivity : FragmentActivity() {
    private lateinit var container: FrameLayout
    private lateinit var button: Button

    companion object {
        private var currentlyShowingSurveys = true

        private var surveyFragment = SurveyFragment()
        private var transactionFragment = TransactionFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container = findViewById(R.id.fragment_container)
        button = findViewById(R.id.toggleButton)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, surveyFragment)
            .commit()

        button.setOnClickListener {
            currentlyShowingSurveys = !currentlyShowingSurveys
            if (currentlyShowingSurveys) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(transactionFragment)
                    .add(R.id.fragment_container, surveyFragment)
                    .commit()
            } else {
                supportFragmentManager
                    .beginTransaction()
                    .remove(surveyFragment)
                    .add(R.id.fragment_container, transactionFragment)
                    .commit()
            }
        }

        (application as? CPXApplication)?.let {
            it.cpxResearch().setSurveyVisibleIfAvailable(true, this)
            it.cpxResearch().registerListener(object : CPXResearchListener {
                override fun onSurveysUpdated() {
                    Log.d("CPX", "surveys updated.")
                }

                override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) {
                    Log.d("CPX", "transactions updated.")
                }

                override fun onSurveysDidOpen() {
                    Log.d("CPX", "surveys opened.")
                }

                override fun onSurveysDidClose() {
                    Log.d("CPX", "surveys closed.")
                    it.cpxResearch().requestSurveyUpdate(true)
                }
            })
        }
    }
}