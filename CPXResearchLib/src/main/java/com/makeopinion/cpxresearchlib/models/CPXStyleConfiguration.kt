package com.makeopinion.cpxresearchlib.models

import android.view.Gravity
import java.io.Serializable

class CPXStyleConfiguration(
    val position: SurveyPosition,
    val text: String,
    val textSize: Int,
    val textColor: String,
    val backgroundColor: String,
    val roundedCorners: Boolean
): Serializable {
    fun getType(): String {
        return when (position) {
            SurveyPosition.SideLeftNormal, SurveyPosition.SideLeftSmall, SurveyPosition.SideRightNormal, SurveyPosition.SideRightSmall -> "side"
            SurveyPosition.CornerBottomLeft, SurveyPosition.CornerTopLeft, SurveyPosition.CornerBottomRight, SurveyPosition.CornerTopRight -> "corner"
            SurveyPosition.ScreenCenterBottom, SurveyPosition.ScreenCenterTop -> "screen"
        }
    }

    fun getPosition(): String {
        return when (position) {
            SurveyPosition.SideLeftNormal, SurveyPosition.SideLeftSmall -> "left"
            SurveyPosition.SideRightNormal, SurveyPosition.SideRightSmall -> "right"
            SurveyPosition.CornerTopLeft -> "topleft"
            SurveyPosition.CornerTopRight -> "topright"
            SurveyPosition.CornerBottomRight -> "bottomright"
            SurveyPosition.CornerBottomLeft -> "bottomleft"
            SurveyPosition.ScreenCenterTop -> "top"
            SurveyPosition.ScreenCenterBottom -> "bottom"
        }
    }

    fun getWidth(): Int {
        return when (position) {
            SurveyPosition.SideRightNormal, SurveyPosition.SideLeftNormal -> 60
            SurveyPosition.SideRightSmall, SurveyPosition.SideLeftSmall -> 30
            SurveyPosition.CornerTopLeft, SurveyPosition.CornerTopRight, SurveyPosition.CornerBottomRight, SurveyPosition.CornerBottomLeft -> 160
            SurveyPosition.ScreenCenterTop, SurveyPosition.ScreenCenterBottom -> 320
        }
    }

    fun getHeight(): Int {
        return when (position) {
            SurveyPosition.SideRightNormal, SurveyPosition.SideLeftNormal, SurveyPosition.SideRightSmall, SurveyPosition.SideLeftSmall -> 400
            SurveyPosition.CornerTopLeft, SurveyPosition.CornerTopRight, SurveyPosition.CornerBottomRight, SurveyPosition.CornerBottomLeft -> 160
            SurveyPosition.ScreenCenterTop, SurveyPosition.ScreenCenterBottom -> 72
        }
    }

    fun getGravity(): Int {
        return when (position) {
            SurveyPosition.SideLeftNormal, SurveyPosition.SideLeftSmall -> Gravity.START or Gravity.CENTER
            SurveyPosition.SideRightNormal, SurveyPosition.SideRightSmall -> Gravity.END or Gravity.CENTER
            SurveyPosition.CornerTopLeft -> Gravity.START or Gravity.TOP
            SurveyPosition.CornerTopRight -> Gravity.END or Gravity.TOP
            SurveyPosition.CornerBottomRight -> Gravity.END or Gravity.BOTTOM
            SurveyPosition.CornerBottomLeft -> Gravity.START or Gravity.BOTTOM
            SurveyPosition.ScreenCenterTop -> Gravity.CENTER or Gravity.TOP
            SurveyPosition.ScreenCenterBottom -> Gravity.CENTER or Gravity.BOTTOM
        }
    }
}

enum class SurveyPosition {
    SideLeftNormal,
    SideLeftSmall,
    SideRightNormal,
    SideRightSmall,
    CornerTopLeft,
    CornerTopRight,
    CornerBottomRight,
    CornerBottomLeft,
    ScreenCenterTop,
    ScreenCenterBottom
}