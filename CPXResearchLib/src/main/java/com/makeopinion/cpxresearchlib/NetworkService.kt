package com.makeopinion.cpxresearchlib

import android.net.Uri
import androidx.core.net.toUri
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.makeopinion.cpxresearchlib.misc.CPXJsonValidator
import com.makeopinion.cpxresearchlib.models.CPXConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyModel


class NetworkService {

    companion object {
        private const val SURVEY_WEB_URL = "https://offers.cpx-research.com/index.php"
        private const val SURVEY_API_URL = "https://live-api.cpx-research.com/api/get-surveys.php"
        private const val IMAGE_API_URL = "https://dyn-image.cpx-research.com/image"

        fun imageUrlFor(configuration: CPXConfiguration): Uri {
            val queryItems = configuration.queryItems()

            val builder = IMAGE_API_URL.toUri().buildUpon()
            queryItems.keys.forEach { builder.appendQueryParameter(it, queryItems[it]) }

            return builder.build()
        }

        fun surveyUrl(configuration: CPXConfiguration, showSurveyById: String? = null): Uri? {
            val queryItems = configuration.queryItems()
            queryItems["no_close"] = "true"
            showSurveyById?.let { queryItems["survey_id"] = it }

            val builder = SURVEY_WEB_URL.toUri().buildUpon()
            queryItems.keys.forEach { builder.appendQueryParameter(it, queryItems[it]) }

            return builder.build()
        }

        fun hideDialogUrl(configuration: CPXConfiguration): Uri? {
            val queryItems = configuration.queryItems()
            queryItems["no_close"] = "true"
            queryItems["site"] = "settings-webview"

            val builder = SURVEY_WEB_URL.toUri().buildUpon()
            queryItems.keys.forEach { builder.appendQueryParameter(it, queryItems[it]) }

            return builder.build()
        }

        fun helpSiteUrl(configuration: CPXConfiguration): Uri? {
            val queryItems = configuration.queryItems()
            queryItems["no_close"] = "true"
            queryItems["site"] = "help"

            val builder = SURVEY_WEB_URL.toUri().buildUpon()
            queryItems.keys.forEach { builder.appendQueryParameter(it, queryItems[it]) }

            return builder.build()
        }
    }

    fun requestSurveysFromApi(
        configuration: CPXConfiguration,
        additionalQueryItems: HashMap<String, String?>?,
        listener: ResponseListener
    ) {
        val queryItems = configuration.queryItems()
        queryItems["output_method"] = "jsscriptv1"
        additionalQueryItems?.let { queryItems.putAll(it) }

        val request = AndroidNetworking.get(SURVEY_API_URL)
            .addQueryParameter(queryItems)
            .build()
        CPXLogger.l("Calling url ${request.url}")

        request.getAsString(object : StringRequestListener {
                override fun onResponse(json: String?) {
                    json?.let { jsonString ->
                        try {
                            val model = Gson().fromJson(jsonString, SurveyModel::class.java)
                            if (model != null && CPXJsonValidator.isValidSurveyModel(model))
                                listener.onSurveyResponse(model)
                        } catch(e: JsonSyntaxException) {
                            //ignore but log at least
                            listener.onError(ANError(e))
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    anError?.let { listener.onError(it) }
                }
            })
    }
}

interface ResponseListener {
    fun onSurveyResponse(model: SurveyModel)
    fun onError(anError: ANError)
}