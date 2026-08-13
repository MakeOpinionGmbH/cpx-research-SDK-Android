package com.makeopinion.cpxresearchlib.models

import java.io.Serializable

class CPXWebViewConfiguration @JvmOverloads constructor(
    val allowHelpButton: Boolean = true,
    val allowSettingsButton: Boolean = true,
    val allowHomeButton: Boolean = true
) : Serializable {
}