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
            "Verdiene bis zu 3 Coins in<br> 4 Minuten mit Umfragen",
            20,
            "#ffffff",
            "ffaf20",
            true)

        val config = CPXConfigurationBuilder(
            "7298",
            "1",
            "1R8kvYZ4joIbhCMPeVf0zaaB2GXSHSVs",
            style)
            .build()

        val cpx = CPXResearch.init(context, config)

        cpx.requestSurveyUpdate(true)

        cpxResearch = cpx
    }
}