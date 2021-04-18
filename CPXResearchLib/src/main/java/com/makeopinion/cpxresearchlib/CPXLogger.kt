package com.makeopinion.cpxresearchlib

import com.makeopinion.cpxresearchlib.models.CPXLogEntry
import java.text.SimpleDateFormat
import java.util.*

object CPXLogger {

    private var logEntries = mutableListOf<CPXLogEntry>()
    private var isEnabled = false

    fun setEnabled(value: Boolean) {
        isEnabled = value
    }

    fun f(name: String) {
        if (isEnabled)
            logEntries.add(CPXLogEntry("${timestamp()}: Method called: $name"))
    }

    fun l(message: String) {
        if (isEnabled)
            logEntries.add(CPXLogEntry("${timestamp()}: Log: $message"))
    }

    fun getLogEntries(): List<String> {
        return logEntries.map { it.value }
    }

    private fun timestamp(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}