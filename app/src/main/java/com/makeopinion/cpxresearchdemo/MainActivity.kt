package com.makeopinion.cpxresearchdemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyPosition
import com.makeopinion.cpxresearchlib.models.TransactionItem


class MainActivity : FragmentActivity() {
    private lateinit var container: FrameLayout
    private lateinit var btnToggleList: Button
    private lateinit var btnNextStyle: Button

    private var surveyFragment = SurveyFragment()
    private var transactionFragment = TransactionFragment()

    private var currentStyleIndex = 0

    companion object {
        private var currentlyShowingSurveys = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container = findViewById(R.id.fragment_container)
        btnToggleList = findViewById(R.id.toggleButton)
        btnNextStyle = findViewById(R.id.styleButton)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, surveyFragment)
            .commit()

        btnToggleList.setOnClickListener {
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

        btnNextStyle.setOnClickListener {
            switchToNextStyle()
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

    private fun switchToNextStyle() {
        currentStyleIndex++

        if (currentStyleIndex == SurveyPosition.values().size)
            currentStyleIndex = 0

        (application as? CPXApplication)?.cpxResearch()?.let {
            val style = CPXStyleConfiguration(
                    SurveyPosition.values()[currentStyleIndex],
                    "New surveys available",
                    20,
                    "#ffffff",
                    "#ffaf20",
                    true)
            it.setStyle(style)
        }
    }
}