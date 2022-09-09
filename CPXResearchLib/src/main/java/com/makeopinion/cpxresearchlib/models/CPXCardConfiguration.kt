package com.makeopinion.cpxresearchlib.models

import android.app.Activity
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.makeopinion.cpxresearchlib.R

class CPXCardConfiguration(
    @ColorInt val accentColor: Int,
    @ColorInt val backgroundColor: Int,
    @ColorInt val inactiveStarColor: Int,
    @ColorInt val starColor: Int,
    @ColorInt val textColor: Int,
    @ColorInt val dividerColor: Int,
    val cardsOnScreen: Int,
    @ColorInt val promotionAmountColor: Int,
    val cornerRadius: Float,
    val maximumItems: Int,
    val paddingLeft: Float,
    val paddingRight: Float,
    val paddingTop: Float,
    val paddingBottom: Float,
    val cpxCardStyle: CPXCardStyle,
    val fixedWidth: Int,
    @DrawableRes val currencyPrefixImage: Int?,
    val hideCurrencyName: Boolean,
    val hideRatingAmount: Boolean,
    val showCurrencyBeforeValue: Boolean
) {
    class Builder {
        @ColorInt private var accentColor: Int = Color.parseColor("#41d7e5")
        @ColorInt private var backgroundColor: Int = Color.WHITE
        @ColorInt private var inactiveStarColor: Int = Color.parseColor("#dfdfdf")
        @ColorInt private var starColor: Int = Color.parseColor("#ffaa00")
        @ColorInt private var textColor: Int = Color.DKGRAY
        @ColorInt private var dividerColor: Int = Color.parseColor("#5A7DFE")

        private var cardsOnScreen: Int = 3
        private var promotionAmountColor: Int = Color.RED
        private var cornerRadius: Float = 10f
        private var maximumItems: Int = Integer.MAX_VALUE

        private var paddingLeft: Float = 0f
        private var paddingRight: Float = 0f
        private var paddingTop: Float = 0f
        private var paddingBottom: Float = 0f

        private var cpxCardStyle: CPXCardStyle = CPXCardStyle.DEFAULT
        @DrawableRes private var currencyPrefixImage: Int? = null

        private var fixedWidth: Int = 0

        private var hideCurrencyName: Boolean = false
        private var hideRatingAmount: Boolean = true
        private var showCurrencyBeforeValue: Boolean = false

        /**
         * Color the amount and currency is in.
         *
         * @param color The new accent color.
         * @return This Builder instance updated with the set color.
         */
        fun accentColor(@ColorInt color: Int) = apply { this.accentColor = color }

        /**
         * The background color of the card.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun backgroundColor(@ColorInt color: Int) = apply { this.backgroundColor = color }

        /**
         * The color of inactive stars.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun inactiveStarColor(@ColorInt color: Int) = apply { this.inactiveStarColor = color }

        /**
         * The color of active stars in the rating.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun starColor(@ColorInt color: Int) = apply { this.starColor = color }

        /**
         * The text color for the estimated length the survey takes to complete.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun textColor(@ColorInt color: Int) = apply { this.textColor = color }

        /**
         * The divider color for the left vertical color bar on small CPXCardStyle.
         *
         * @param color The new color value.
         * @return This Builder instance updated with the set color.
         */
        fun dividerColor(@ColorInt color: Int) = apply { this.dividerColor = color }

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
         * Sets the card style used.
         *
         * @param style The new CPXCardStyle.
         * @return This Builder instance.
         */
        fun cpxCardStyle(style: CPXCardStyle) = apply { this.cpxCardStyle = style }

        /**
         * Set the card width to a fixed value.
         * Set it to 0 to auto calculate based on screen width and items that should be visible.
         *
         * @param width The fixed width in dp.
         * @return This Builder instance.
         */
        fun fixedCPXCardWidth(width: Int) = apply { this.fixedWidth = width }

        /**
         * Sets an 15dp x 17dp icon in front of the currency value on small CPXCardStyle.
         *
         * @param res The image resource to set in front of the currency value String.
         * @return This Builder instance.
         */
        fun currencyPrefixImage(@DrawableRes res: Int) = apply { this.currencyPrefixImage = res }

        /**
         * Hides/Shows the currency name.
         *
         * @param boolean Set to true to hide the currency name.
         * @return This Builder instance.
         */
        fun hideCurrencyName(boolean: Boolean) = apply { this.hideCurrencyName = boolean }

        /**
         * Hides/Shows the amount of ratings in total.
         *
         * @param boolean Set to true to hide the amount of ratings.
         * @return This Builder instance.
         */
        fun hideRatingAmount(boolean: Boolean) = apply { this.hideRatingAmount = boolean }

        /**
         * Show currency name/symbol in front of the amount.
         *
         * @param boolean If set to true the currency name will be shown in before the amount value.
         * @return This Builder instance.
         */
        fun showCurrencyBeforeValue(boolean: Boolean) = apply { this.showCurrencyBeforeValue = boolean }

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
                dividerColor,
                cardsOnScreen,
                promotionAmountColor,
                cornerRadius,
                maximumItems,
                paddingLeft,
                paddingRight,
                paddingTop,
                paddingBottom,
                cpxCardStyle,
                fixedWidth,
                currencyPrefixImage,
                hideCurrencyName,
                hideRatingAmount,
                showCurrencyBeforeValue)
    }

    fun getWidth(activity: Activity): Int {
        val density = activity.resources.displayMetrics.density
        return if (fixedWidth == 0) {
            val paddingLeftInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingLeft, activity.resources.displayMetrics).toInt()
            val paddingRightInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingRight, activity.resources.displayMetrics).toInt()
            val marginInPx = 2 * cardsOnScreen * 4 * density + paddingLeftInPx + paddingRightInPx
            val elementWidth = maxOf((activity.resources.displayMetrics.widthPixels - marginInPx) / cardsOnScreen, 80.0f * density)

            elementWidth.toInt()
        } else {
            val elementWidth = fixedWidth * density
            elementWidth.toInt()
        }
    }
}

enum class CPXCardStyle(@LayoutRes val resource: Int) {
    DEFAULT(R.layout.cpxresearchcard),
    SMALL(R.layout.cpxresearchsmallcard)
}