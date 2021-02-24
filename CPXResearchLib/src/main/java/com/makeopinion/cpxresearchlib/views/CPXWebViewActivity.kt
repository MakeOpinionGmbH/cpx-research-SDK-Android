package com.makeopinion.cpxresearchlib.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import com.google.gson.Gson
import com.makeopinion.cpxresearchlib.NetworkService
import com.makeopinion.cpxresearchlib.R
import com.makeopinion.cpxresearchlib.models.CPXConfiguration
import com.makeopinion.cpxresearchlib.models.SupportModel
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream
import java.net.URL


class CPXWebViewActivity : Activity() {

    private var webView: WebView? = null
    private var btnClose: ImageView? = null
    private var btnSettings: ImageView? = null
    private var btnHelp: ImageView? = null

    private var configuration: CPXConfiguration? = null
    private var screenshot: Bitmap? = null

    companion object {
        private var listener: CPXWebActivityListener? = null
        fun launchSurveysActivity(activity: Activity,
                                  configuration: CPXConfiguration,
                                  listener: CPXWebActivityListener) {
            val intent = Intent(activity, CPXWebViewActivity::class.java)
            val url = NetworkService.surveyUrl(configuration).toString()
            intent.putExtra("url", url)
            intent.putExtra("config", configuration)
            intent.putExtra("onlyCloseButtonVisible", false)
            activity.startActivity(intent)
            listener.onDidOpen()
            this.listener = listener
        }

        fun launchSingleSurveyActivity(activity: Activity,
                                       configuration: CPXConfiguration,
                                       surveyId: String) {
            val intent = Intent(activity, CPXWebViewActivity::class.java)
            val url = NetworkService.surveyUrl(configuration, surveyId).toString()
            intent.putExtra("url", url)
            intent.putExtra("config", configuration)
            intent.putExtra("onlyCloseButtonVisible", false)
            activity.startActivity(intent)
        }

        fun launchHideDialogActivity(activity: Activity, configuration: CPXConfiguration) {
            val intent = Intent(activity, CPXWebViewActivity::class.java)
            val url = NetworkService.hideDialogUrl(configuration).toString()
            intent.putExtra("url", url)
            intent.putExtra("config", configuration)
            intent.putExtra("onlyCloseButtonVisible", true)
            activity.startActivity(intent)
        }

        fun launchHelpDialogActivity(activity: Activity,
                                     configuration: CPXConfiguration,
                                     screenshotFilename: String? = null) {
            val intent = Intent(activity, CPXWebViewActivity::class.java)
            val url = NetworkService.helpSiteUrl(configuration).toString()
            intent.putExtra("url", url)
            intent.putExtra("config", configuration)
            intent.putExtra("onlyCloseButtonVisible", true)
            screenshotFilename?.let { intent.putExtra("screenshot", screenshotFilename) }
            activity.startActivity(intent)
        }

        private fun takeScreenshotOf(view: View): Bitmap? {
            val bitmap = Bitmap.createBitmap(view.width,
                    view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        private fun saveScreenshot(activity: Activity, bitmap: Bitmap): String {
            val filename = "screenshot_help.png"
            val stream = activity.openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            stream.close()
            bitmap.recycle()

            return filename
        }

        private fun loadScreenshot(activity: Activity, filename: String): Bitmap {
            val stream = activity.openFileInput(filename)
            val bitmap = BitmapFactory.decodeStream(stream)
            stream.close()

            return bitmap
        }

        private fun bitmapToString(bitmap: Bitmap): String {
            val byteArrayBitmapStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream)
            val b: ByteArray = byteArrayBitmapStream.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpxwebview)

        configuration = intent.getSerializableExtra("config") as CPXConfiguration

        webView = findViewById(R.id.webView)
        btnClose = findViewById(R.id.btn_close)
        btnSettings = findViewById(R.id.btn_settings)
        btnHelp = findViewById(R.id.btn_help)
    }

    override fun onStart() {
        super.onStart()
        if (intent.getBooleanExtra("onlyCloseButtonVisible", true)) {
            btnHelp?.visibility = View.GONE
            btnSettings?.visibility = View.GONE
        }

        intent.getStringExtra("screenshot")?.let {
            screenshot = loadScreenshot(this, it)
        }

        webView?.let { webView ->
            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = false
            webView.settings.domStorageEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url)
                    return true
                }
            }
            screenshot?.let {
                doAsync {
                    val supportModel = SupportModel(emptyList(),
                            bitmapToString(it))
                    val json = Gson().toJson(supportModel)

                    val client = OkHttpClient()
                    val uri = URL(intent.getStringExtra("url"))
                    val body = RequestBody.create(MediaType.parse("application/json"), json)

                    val request = Request.Builder()
                            .url(uri)
                            .post(body)
                            .build()

                    val response = client.newCall(request).execute()

                    response.body()?.let { body ->
                        runOnUiThread {
                            webView.loadDataWithBaseURL(intent.getStringExtra("url"),
                                    body.string(),
                                    "text/html",
                                    "UTF-8",
                                    null)
                        }
                    }
                }
            } ?: run {
                webView.loadUrl(intent.getStringExtra("url"))
            }
        }

        btnClose?.let {
            it.setOnClickListener {
                this.finish()
            }
        }

        btnSettings?.let {
            it.setOnClickListener {
                configuration?.let { config ->
                    launchHideDialogActivity(this, config)
                }
            }
        }

        btnHelp?.let { btnHelp ->
            btnHelp.setOnClickListener {
                webView?.also {
                    configuration?.let { config ->
                        takeScreenshotOf(webView!!)?.let {
                            val filename = saveScreenshot(this, it)
                            launchHelpDialogActivity(this, config, filename)
                        } ?: run {
                            launchHelpDialogActivity(this, config)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.onDidClose()
    }
}

interface CPXWebActivityListener {
    fun onDidOpen()
    fun onDidClose()
}