package com.makeopinion.cpxresearchdemo

import android.app.Application
import com.makeopinion.cpxresearchlib.CPXResearch
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyPosition

class CPXApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initCPX()
    }

    private var cpxResearch: CPXResearch? = null

    fun cpxResearch(): CPXResearch {
        return cpxResearch!!
    }

    private fun initCPX() {
        val style = CPXStyleConfiguration(
            SurveyPosition.CornerBottomLeft,
            "Earn Coins",
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

        val cpx = CPXResearch.init(config)
        cpx.setLogMode(true)
        cpxResearch = cpx
    }
}