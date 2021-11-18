package com.makeopinion.cpxresearchlib.models

import android.graphics.Color

class CPXCardConfiguration(
    val accentColor: Int,
    val backgroundColor: Int,
    val inactiveStarColor: Int,
    val starColor: Int,
    val textColor: Int,
    val cardsOnScreen: Int,
    val promotionAmountColor: Int,
    val cornerRadius: Float,
    val maximumItems: Int,
    val paddingLeft: Float,
    val paddingRight: Float,
    val paddingTop: Float,
    val paddingBottom: Float
) {
    class Builder {
        private var accentColor: Int = Color.parseColor("#41d7e5")
        private var backgroundColor: Int = Color.WHITE
        private var inactiveStarColor: Int = Color.parseColor("#dfdfdf")
        private var starColor: Int = Color.parseColor("#ffaa00")
        private var textColor: Int = Color.DKGRAY
        private var cardsOnScreen: Int = 3
        private var promotionAmountColor: Int = Color.RED
        private var cornerRadius: Float = 10f
        private var maximumItems: Int = Integer.MAX_VALUE

        private var paddingLeft: Float = 0f
        private var paddingRight: Float = 0f
        private var paddingTop: Float = 0f
        private var paddingBottom: Float = 0f

        /**
         * Color the amount and currency is in.
         *
         * @param color The new accent color.
         * @return This Builder instance updated with the set color.
         */
        fun accentColor(color: Int) = apply { this.accentColor = color }

        /**
         * The background color of the card.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun backgroundColor(color: Int) = apply { this.backgroundColor = color }

        /**
         * The color of inactive stars.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun inactiveStarColor(color: Int) = apply { this.inactiveStarColor = color }

        /**
         * The color of active stars in the rating.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun starColor(color: Int) = apply { this.starColor = color }

        /**
         * The text color for the estimated length the survey takes to complete.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun textColor(color: Int) = apply { this.textColor = color }

        /**
         * How many cards should be visible on screen.
         *
         * @param amount The amount of cards that should be visible on screen.
         * @return This Builder instance updated with the cards that should be visible on screen.
         */
        fun cardsOnScreen(amount: Int) = apply { this.cardsOnScreen = amount }

        /**
         * The color of the original amount if there is a promotion active.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun promotionAmountColor(color: Int) = apply { this.promotionAmountColor = color }

        /**
         * The radius of the rounded corners of a CPX Card in dp.
         *
         * @param radius The new corner radius.
         * @return This Builder instance updated with the set corner radius.
         */
        fun cornerRadius(radius: Float) = apply { this.cornerRadius = radius }

        /**
         * Sets the maximum amount of surveys that should be displayed as Cards.
         *
         * @param amount The new amount value.
         * @return This Builder instance updated with the set maximum amount of surveys.
         */
        fun maximumSurveys(amount: Int) = apply { this.maximumItems = amount }

        /**
         * The padding the CPX Cards keep from the left border in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun paddingLeft(value: Float) = apply { this.paddingLeft = value }

        /**
         * The padding the CPX Cards keep from the right border in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun paddingRight(value: Float) = apply { this.paddingRight = value }

        /**
         * The padding the CPX Cards keep from the top border in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun paddingTop(value: Float) = apply { this.paddingTop = value }

        /**
         * The padding the CPX Cards keep from the bottom border in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun paddingBottom(value: Float) = apply { this.paddingBottom = value }

        /**
         * The padding the CPX Cards keep from the left and right border in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun paddingHorizontal(value: Float) = apply {
            this.paddingLeft = value
            this.paddingRight = value
        }

        /**
         * The padding the CPX Cards keep from the top and bottom border in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun paddingVertical(value: Float) = apply {
            this.paddingTop = value
            this.paddingBottom = value
        }

        /**
         * The padding the CPX Cards keep from the all borders in dp.
         *
         * @param value The new padding value.
         * @return This Builder instance updated with the set padding.
         */
        fun padding(value: Float) = apply {
            this.paddingLeft = value
            this.paddingRight = value
            this.paddingTop = value
            this.paddingBottom = value
        }

        /**
         * Generate the CPXCardConfiguration object.
         *
         * @return the CPXCardConfiguration with the set values.
         */
        fun build() = CPXCardConfiguration(accentColor,
                backgroundColor,
                inactiveStarColor,
                starColor,
                textColor,
                cardsOnScreen,
                promotionAmountColor,
                cornerRadius,
                maximumItems,
                paddingLeft,
                paddingRight,
                paddingTop,
                paddingBottom)
    }
}