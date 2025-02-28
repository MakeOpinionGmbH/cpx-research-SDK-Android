package com.makeopinion.cpxresearchlib.models

import com.makeopinion.cpxresearchlib.misc.CPXHash
import com.makeopinion.cpxresearchlib.misc.prefixedDot
import com.makeopinion.cpxresearchlib.misc.toPx
import java.io.Serializable

class CPXConfiguration(
        val appId: String,
        val extUserId: String,
        val secureHash: String,
        val email: String?,
        val subId1: String?,
        val subId2: String?,
        val extraInfo: Array<String>?,
        var style: CPXStyleConfiguration,
        val confirmDialogTitle: String?,
        val confirmDialogMsg: String?,
        val confirmDialogLeaveBtnText: String?,
        val confirmDialogCancelBtnText: String?
): Serializable {
        constructor(builder: CPXConfigurationBuilder) : this(
                builder.appId,
                builder.extUserId,
                builder.secureHash,
                builder.email,
                builder.subId1,
                builder.subId2,
                builder.extraInfo,
                builder.style,
                builder.confirmDialogTitle,
                builder.confirmDialogMsg,
                builder.confirmDialogLeaveBtnText,
                builder.confirmDialogCancelBtnText
        )

        fun queryItems(): HashMap<String, String?> {
                val result = hashMapOf(
                        Pair("app_id", appId),
                        Pair("ext_user_id", extUserId),
                        Pair("type", style.getType()),
                        Pair("position", style.getPosition()),
                        Pair("backgroundcolor", style.backgroundColor.prefixedDot()),
                        Pair("textcolor", style.textColor.prefixedDot()),
                        Pair("rounded_corners", style.roundedCorners.toString()),
                        Pair("width", style.getWidth().toPx().toString()),
                        Pair("height", style.getHeight().toPx().toString()),
                        Pair("emptycolor", null),
                        Pair("transparent", "1"),
                        Pair("text", style.text),
                        Pair("textsize", "${style.textSize.toPx()}"),
                        Pair("sdk", "android"),
                        Pair("sdk_version", "1.6.0"),
                        Pair("secure_hash", CPXHash.md5("${extUserId}-${secureHash}"))
                )

                email?.let { result["email"] = it }
                subId1?.let { result["subid1"] = it }
                subId2?.let { result["subid2"] = it }
                extraInfo?.let { it.forEachIndexed { index, value -> result["extra${index + 1}"] = value } }

                return result
        }
}

class CPXConfigurationBuilder(val appId: String,
                              val extUserId: String,
                              val secureHash: String,
                              val style: CPXStyleConfiguration
) {
        var email: String? = null
                private set
        var subId1: String? = null
                private set
        var subId2: String? = null
                private set
        var extraInfo: Array<String>? = null
                private set
        var confirmDialogTitle: String? = "Leave Survey"
                private set
        var confirmDialogMsg: String? = "Once you leave this survey, you won\'t be able to try it again.\n\nDo you want to continue?"
                private set
        var confirmDialogLeaveBtnText: String? = "Leave Survey"
                private set
        var confirmDialogCancelBtnText: String? = "Cancel"
                private set

        fun withEmail(email: String) = apply { this.email = email }

        fun withSubId1(subId: String) = apply { this.subId1 = subId }

        fun withSubId2(subId: String) = apply { this.subId2 = subId }

        fun withExtraInfo(extraInfo: Array<String>) = apply { this.extraInfo = extraInfo }

        fun withCustomConfirmCloseDialogTexts(title: String?,
                                              msg: String?,
                                              leaveButtonText: String?,
                                              cancelButtonText: String?) = apply {
                this.confirmDialogTitle = title
                this.confirmDialogMsg = msg
                this.confirmDialogLeaveBtnText = leaveButtonText
                this.confirmDialogCancelBtnText = cancelButtonText
        }

        fun build() = CPXConfiguration(this)
}
