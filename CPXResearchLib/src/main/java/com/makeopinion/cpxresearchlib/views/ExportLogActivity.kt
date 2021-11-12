package com.makeopinion.cpxresearchlib.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.makeopinion.cpxresearchlib.CPXLogger
import com.makeopinion.cpxresearchlib.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ExportLogActivity: Activity() {

    private val CREATE_FILE = 1

    private var textview: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_log)

        textview = findViewById(R.id.textview)

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "cpxresearch.log")
        }
        startActivityForResult(intent, CREATE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FILE
            && resultCode == Activity.RESULT_OK) {
            try {
                data?.data?.also { uri ->
                    contentResolver.openFileDescriptor(uri, "w")?.use {
                        FileOutputStream(it.fileDescriptor).use {
                            CPXLogger.getLogEntries().forEach { text ->
                                it.write("$text\n".toByteArray())
                            }
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            textview?.let {
                it.text = "Log file exported."
                Handler().postDelayed({
                    finish()
                }, 1500)
            }
        }
    }
}