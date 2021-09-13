package com.makeopinion.cpxresearchlib

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.makeopinion.cpxresearchlib.misc.CPXJsonValidator
import com.makeopinion.cpxresearchlib.models.CPXConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyModel
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException


class NetworkService {

    companion object {
        private const val SURVEY_WEB_URL = "https://offers.cpx-research.com/index.php"
        private const val SURVEY_API_URL = "https://live-api.cpx-research.com/api/get-surveys.php"
        private const val IMAGE_API_URL = "https://dyn-image.cpx-research.com/image"

        private var httpClient = OkHttpClient()

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

        val urlBuilder = SURVEY_API_URL.toHttpUrl()
                    .newBuilder()
        queryItems.keys.forEach { urlBuilder.addQueryParameter(it, queryItems[it]) }

        val request = Request.Builder()
                .url(urlBuilder.build())
                .build()

        CPXLogger.l("Calling url ${request.url}")

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { res ->
                    if (!res.isSuccessful) throw IOException("Unexpected code $res")

                    res.body?.let {
                        try {
                            val model = Gson().fromJson(it.string(), SurveyModel::class.java)
                            if (model != null && CPXJsonValidator.isValidSurveyModel(model))
                                Handler(Looper.getMainLooper()).post { listener.onSurveyResponse(model) }
                        } catch(e: JsonSyntaxException) {
                            //ignore but log at least
                            listener.onError(e)
                        }
                    }
                }
            }
        })
    }
}

interface ResponseListener {
    fun onSurveyResponse(model: SurveyModel)
    fun onError(anError: Exception)
}