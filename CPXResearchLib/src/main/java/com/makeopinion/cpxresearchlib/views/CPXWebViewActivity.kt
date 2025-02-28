package com.makeopinion.cpxresearchlib.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.security.NetworkSecurityPolicy
import android.util.Base64
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.gson.Gson
import com.makeopinion.cpxresearchlib.NetworkService
import com.makeopinion.cpxresearchlib.R
import com.makeopinion.cpxresearchlib.models.CPXConfiguration
import com.makeopinion.cpxresearchlib.models.SupportModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException


class CPXWebViewActivity : Activity() {

    private var bgLinearLayout: LinearLayout? = null
    private var webView: WebView? = null
    private var btnClose: ImageView? = null
    private var btnSettings: ImageView? = null
    private var btnHelp: ImageView? = null
    private var btnHome: ImageView? = null
    private var progressBar: ProgressBar? = null

    private var configuration: CPXConfiguration? = null
    private var calledUrls = mutableListOf<String>()
    private var screenshot: Bitmap? = null

    private var confirmDialogTitle: String? = null
    private var confirmDialogMsg: String? = null
    private var confirmDialogLeaveBtnText: String? = null
    private var confirmDialogCancelBtnText: String? = null

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
            intent.putExtra("confirmCloseDialog", true)
            activity.startActivity(intent)
            listener.onDidOpen()
            this.listener = listener
        }

        fun launchSingleSurveyActivity(activity: Activity,
                                       configuration: CPXConfiguration,
                                       surveyId: String,
                                       listener: CPXWebActivityListener) {
            val intent = Intent(activity, CPXWebViewActivity::class.java)
            val url = NetworkService.surveyUrl(configuration, surveyId).toString()
            intent.putExtra("url", url)
            intent.putExtra("config", configuration)
            intent.putExtra("onlyCloseButtonVisible", false)
            intent.putExtra("confirmCloseDialog", true)
            activity.startActivity(intent)
            listener.onDidOpen()
            this.listener = listener
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

        bgLinearLayout = findViewById(R.id.bg)
        webView = findViewById(R.id.webView)
        btnClose = findViewById(R.id.btn_close)
        btnSettings = findViewById(R.id.btn_settings)
        btnHelp = findViewById(R.id.btn_help)
        btnHome = findViewById(R.id.btn_home)
        progressBar = findViewById(R.id.progressBar)

        confirmDialogTitle = configuration?.confirmDialogTitle
        confirmDialogMsg = configuration?.confirmDialogMsg
        confirmDialogLeaveBtnText = configuration?.confirmDialogLeaveBtnText
        confirmDialogCancelBtnText = configuration?.confirmDialogCancelBtnText

        val color = Color.parseColor(configuration!!.style.backgroundColor)
        progressBar?.progressTintList = ColorStateList.valueOf(color)
        bgLinearLayout?.setBackgroundColor(color)

        setupContent()
        setupWebView()

        val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        btnHome?.background?.colorFilter = colorFilter
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.onDidClose()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            val showCloseConfirm = intent.getBooleanExtra("confirmCloseDialog", false)
            if (showCloseConfirm) {
                showCloseWarning()
            } else {
                finish()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView?.let { webView ->
            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = false
            webView.settings.domStorageEnabled = true
            webView.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        calledUrls.add(it)
                    }

                    if (url != null) {
                        var isCleartextTrafficPermitted = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            isCleartextTrafficPermitted = NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted
                        }
                        if (!url.startsWith("https") && !isCleartextTrafficPermitted) {
                            view?.loadUrl(url.replace("http", "https"))
                            return true
                        }
                    }

                    return false
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)

                    progressBar?.let {
                        if (newProgress < 100 && it.visibility == View.GONE) {
                            it.visibility = View.VISIBLE
                        }

                        it.progress = newProgress

                        if (newProgress == 100) {
                            it.visibility = View.GONE
                        }
                    }
                }
            }
            screenshot?.let {
                val supportModel = SupportModel(calledUrls,
                    bitmapToString(it))
                val json = Gson().toJson(supportModel)

                intent.getStringExtra("url")?.let { url ->
                    val request = Request.Builder()
                        .url(url)
                        .post(json.toRequestBody("application/json".toMediaTypeOrNull()))
                        .build()

                    val client = OkHttpClient()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.body?.string()?.let {
                                runOnUiThread {
                                    webView.loadDataWithBaseURL(
                                        intent.getStringExtra("url"),
                                        it,
                                        "text/html",
                                        "UTF-8",
                                        null
                                    )
                                }
                            }
                        }

                    })
                }
            } ?: run {
                intent.getStringExtra("url")?.let {
                    webView.loadUrl(it)
                }
            }
        }
    }

    private fun setupContent() {
        val showCloseConfirm = intent.getBooleanExtra("confirmCloseDialog", false)

        if (intent.getBooleanExtra("onlyCloseButtonVisible", true)) {
            btnHelp?.visibility = View.GONE
            btnSettings?.visibility = View.GONE
            btnHome?.visibility = View.GONE
        }

        intent.getStringExtra("screenshot")?.let {
            screenshot = loadScreenshot(this, it)
        }

        btnClose?.let {
            it.setOnClickListener {
                if (showCloseConfirm) {
                    showCloseWarning()
                } else {
                    this.finish()
                }
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

        btnHome?.setOnClickListener {
            intent.getStringExtra("url")?.let {
                webView?.loadUrl(it)
            }
        }
    }

    private fun showCloseWarning() {
        if (confirmDialogTitle == null
            && confirmDialogMsg == null
            && confirmDialogLeaveBtnText == null
            && confirmDialogCancelBtnText == null) {
            this.finish()
            return
        }

        val builder: AlertDialog.Builder? = let {
            AlertDialog.Builder(it)
        }
            .setMessage(confirmDialogMsg)
            .setTitle(confirmDialogTitle)
            .setPositiveButton(confirmDialogLeaveBtnText) { _, _ ->
                this.finish()
            }
            .setNegativeButton(confirmDialogCancelBtnText, null)

        val dialog = builder?.create()
        dialog?.show()
    }
}

interface CPXWebActivityListener {
    fun onDidOpen()
    fun onDidClose()
}