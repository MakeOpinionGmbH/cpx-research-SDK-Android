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
            SurveyPosition.CornerBottomRight,
            "Earn Coins",
            20,
            "#ffffff",
            "#ffffff",
            true)

        val config = CPXConfigurationBuilder(
            "5878",
            "1",
            "secureHash",
            style)
            .withSubId1("subId1")
            .withSubId2("subId2")
            .withEmail("user@email.com")
            .withExtraInfo(arrayOf("value1", "value2"))
            .withCustomConfirmCloseDialogTexts("title", "msg", "leave", "cancel")
            .build()

        val cpx = CPXResearch.init(config)
        cpx.setLogMode(true)
        cpxResearch = cpx
    }
}