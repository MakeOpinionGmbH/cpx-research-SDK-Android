package com.makeopinion.cpxresearchdemo

import android.app.Application
import android.content.Context
import android.util.Log
import com.makeopinion.cpxresearchlib.CPXResearch
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyPosition
import com.makeopinion.cpxresearchlib.models.TransactionItem

class CPXApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initCPX(this)
    }

    private var cpxResearch: CPXResearch? = null

    fun cpxResearch(): CPXResearch {
        return cpxResearch!!
    }

    private fun initCPX(context: Context) {
        val style = CPXStyleConfiguration(
            SurveyPosition.SideRightNormal,
            "Earn up to 3 Coins in<br> 4 minutes with surveys",
            20,
            "#ffffff",
            "#ffaf20",
            true)

        val config = CPXConfigurationBuilder(
            "",
            "",
            "",
            style)
            .build()

        val cpx = CPXResearch.init(context, config)

        cpx.requestSurveyUpdate(true)

        cpxResearch = cpx
    }
}