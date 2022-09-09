package com.makeopinion.cpxresearchdemo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.models.*


class MainActivity : FragmentActivity() {
    private lateinit var container: FrameLayout
    private lateinit var btnToggleList: Button
    private lateinit var btnNextStyle: Button
    private lateinit var llContainerNormal: LinearLayout
    private lateinit var llContainerSmall: LinearLayout

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
        llContainerNormal = findViewById(R.id.ll_container_normal)
        llContainerSmall = findViewById(R.id.ll_container_small)

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
                    val surveys = it.cpxResearch().surveys
                    Log.d("CPX", "surveys updated: $surveys")
                }

                override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) {
                    Log.d("CPX", "transactions updated.")
                    for (item in unpaidTransactions) {
                        Log.d("CPX", "${item.earningPublisher}")
                    }
                }

                override fun onSurveysDidOpen() {
                    Log.d("CPX", "surveys opened.")
                }

                override fun onSurveysDidClose() {
                    Log.d("CPX", "surveys closed.")
                    it.cpxResearch().requestSurveyUpdate(true)
                }

                override fun onSurveyDidOpen() {
                    Log.d("CPX", "single survey opened.")
                }

                override fun onSurveyDidClose() {
                    Log.d("CPX", "single survey closed.")
                }
            })
            val cardConfigNormal = CPXCardConfiguration.Builder()
                    .accentColor(Color.parseColor("#4800AA"))
                    .backgroundColor(Color.parseColor("#FFFFFF"))
                    .starColor(Color.parseColor("#FFAA00"))
                    .inactiveStarColor(Color.parseColor("#838393"))
                    .textColor(Color.parseColor("#8E8E93"))
                    .dividerColor(Color.parseColor("#5A7DFE"))
                    //.promotionAmountColor(Color.parseColor("#3D3E4B"))
                    //.cardsOnScreen(4)
                    .cornerRadius(4f)
                    .maximumSurveys(3)
                    //.paddingHorizontal(16f)
                    //.paddingVertical(16f)
                    //.padding(16f)
                    //.paddingLeft(16f)
                    //.paddingRight(16f)
                    //.paddingTop(16f)
                    //.paddingBottom(16f)
                    .cpxCardStyle(CPXCardStyle.DEFAULT)
                    .fixedCPXCardWidth(132)
                    //.currencyPrefixImage(R.drawable.cpx_icon_star)
                    //.hideCurrencyName(true)
                    //.hideRatingAmount(false)
                    //.showCurrencyBeforeValue(true)
                    .build()

            val cardConfigSmall = CPXCardConfiguration.Builder()
                .accentColor(Color.parseColor("#4800AA"))
                .backgroundColor(Color.parseColor("#FFFFFF"))
                .starColor(Color.parseColor("#FFAA00"))
                .inactiveStarColor(Color.parseColor("#838393"))
                .textColor(Color.parseColor("#8E8E93"))
                .dividerColor(Color.parseColor("#5A7DFE"))
                .cornerRadius(4f)
                .cpxCardStyle(CPXCardStyle.SMALL)
                .fixedCPXCardWidth(146)
                .currencyPrefixImage(R.drawable.cpx_icon_star)
                .hideCurrencyName(false)
                //.showCurrencyBeforeValue(true)
                .build()

            it.cpxResearch().insertCPXResearchCardsIntoContainer(this, llContainerNormal, cardConfigNormal)
            it.cpxResearch().insertCPXResearchCardsIntoContainer(this, llContainerSmall, cardConfigSmall)
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