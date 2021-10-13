package com.makeopinion.cpxresearchlib.models

import android.graphics.Color

/**
 * The configuration for CPXCards
 *
 * @property accentColor Color the amount and currency is in.
 * @property backgroundColor The background color of the card.
 * @property inactiveStarColor The color of inactive stars.
 * @property starColor The color of active stars in the rating.
 * @property textColor The text color for the estimated length the survey takes to complete.
 * @property cardsOnScreen How many cards should be visible on screen.
 */
class CPXCardConfiguration(
    val accentColor: Int,
    val backgroundColor: Int,
    val inactiveStarColor: Int,
    val starColor: Int,
    val textColor: Int,
    val cardsOnScreen: Int
) {
    companion object {
        fun default() : CPXCardConfiguration {
            return CPXCardConfiguration(
                Color.parseColor("#41d7e5"),
                Color.WHITE,
                Color.parseColor("#dfdfdf"),
                Color.parseColor("#ffaa00"),
                Color.DKGRAY,
                3)
        }
    }
}