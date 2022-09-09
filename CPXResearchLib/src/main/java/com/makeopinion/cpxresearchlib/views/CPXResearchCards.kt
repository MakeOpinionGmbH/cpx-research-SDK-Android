package com.makeopinion.cpxresearchlib.views

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.makeopinion.cpxresearchlib.CPXResearch
import com.makeopinion.cpxresearchlib.CPXResearchListener
import com.makeopinion.cpxresearchlib.R
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyItem
import com.makeopinion.cpxresearchlib.models.TransactionItem

class CPXResearchCards(private val cpxResearch: CPXResearch,
                       private val config: CPXCardConfiguration,
                       private val elementWidth: Int,
                       private val elementRadius: Float,
                       private var onClickListener: View.OnClickListener) : RecyclerView.Adapter<CPXResearchCards.ViewHolder>(), CPXResearchListener {

    private var items = emptyList<SurveyItem>().toMutableList()

    init {
        cpxResearch.registerListener(this)
        items = cpxResearch.surveys
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(config.cpxCardStyle.resource, parent, false)
        v.setOnClickListener(onClickListener)
        return ViewHolder(v, cpxResearch, config, elementWidth, elementRadius)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return minOf(items.size, config.maximumItems)
    }

    class ViewHolder(itemView: View,
                     private val cpxResearch: CPXResearch,
                     private val config: CPXCardConfiguration,
                     private val elementWidth: Int,
                     private val elementRadius: Float) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(survey: SurveyItem) {
            itemView.tag = survey.id
            val amount = itemView.findViewById(R.id.tv_amount) as TextView
            val amountOriginal = itemView.findViewById(R.id.tv_amount_original) as? TextView
            val currencyPost = itemView.findViewById(R.id.tv_currency_post) as TextView
            val currencyPre = itemView.findViewById(R.id.tv_currency_pre) as TextView
            val time = itemView.findViewById(R.id.tv_time) as TextView
            val timeIcon = itemView.findViewById(R.id.iv_time) as? ImageView
            val star1 = itemView.findViewById(R.id.iv_star1) as ImageView
            val star2 = itemView.findViewById(R.id.iv_star2) as ImageView
            val star3 = itemView.findViewById(R.id.iv_star3) as ImageView
            val star4 = itemView.findViewById(R.id.iv_star4) as ImageView
            val star5 = itemView.findViewById(R.id.iv_star5) as ImageView
            val amountRatings = itemView.findViewById(R.id.tv_amountRatings) as? TextView
            val bg = itemView.findViewById(R.id.cv_container) as CardView
            val currencyPrefixImage = itemView.findViewById(R.id.iv_currency_prefix) as? ImageView
            val divider = itemView.findViewById(R.id.view_divider) as? View

            currencyPost.visibility = View.GONE
            currencyPre.visibility = View.GONE
            val currency = if (config.showCurrencyBeforeValue) currencyPre else currencyPost

            if (survey.hasOfferPayout) {
                amountOriginal?.visibility = View.VISIBLE
                amountOriginal?.text = survey.payout_original
                amountOriginal?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                amountOriginal?.visibility = View.GONE
                amountOriginal?.text = ""
            }

            if (config.currencyPrefixImage != null) {
                currencyPrefixImage?.setImageResource(config.currencyPrefixImage)
                val currencyPrefixImageFilter = PorterDuffColorFilter(config.accentColor, PorterDuff.Mode.SRC_ATOP)
                currencyPrefixImage?.colorFilter = currencyPrefixImageFilter
                currencyPrefixImage?.visibility = View.VISIBLE
            } else {
                currencyPrefixImage?.visibility = View.GONE
            }

            divider?.setBackgroundColor(config.dividerColor)

            bg.setCardBackgroundColor(config.backgroundColor)
            bg.radius = elementRadius
            val layout = bg.layoutParams
            layout.width = elementWidth
            bg.layoutParams = layout

            amount.text = survey.payout
            amount.setTextColor(if (survey.hasOfferPayout) config.promotionAmountColor else config.accentColor)
            amountOriginal?.setTextColor(config.accentColor)
            if (config.hideCurrencyName) {
                currency.text = null
                currency.visibility = View.GONE
            } else {
                currency.text = cpxResearch.cpxText?.currencyNamePlural ?: ""
                currency.setTextColor(config.accentColor)
                currency.visibility = View.VISIBLE
            }
            time.text = "${survey.loi} ${cpxResearch.cpxText?.shortCurtMin ?: "Mins"}"
            time.setTextColor(config.textColor)
            timeIcon?.colorFilter = PorterDuffColorFilter(config.accentColor, PorterDuff.Mode.SRC_ATOP)

            val rating = survey.statisticsRatingAvg
            val activeFilter = PorterDuffColorFilter(config.starColor, PorterDuff.Mode.SRC_ATOP)
            val inactiveFilter = PorterDuffColorFilter(config.inactiveStarColor, PorterDuff.Mode.SRC_ATOP)
            star1.colorFilter = if (rating > 0) activeFilter else inactiveFilter
            star2.colorFilter = if (rating > 1) activeFilter else inactiveFilter
            star3.colorFilter = if (rating > 2) activeFilter else inactiveFilter
            star4.colorFilter = if (rating > 3) activeFilter else inactiveFilter
            star5.colorFilter = if (rating > 4) activeFilter else inactiveFilter

            if (config.hideRatingAmount) {
                amountRatings?.visibility = View.GONE
            } else {
                amountRatings?.visibility = View.VISIBLE
                amountRatings?.text = "(${survey.statisticsRatingCount})"
            }
        }
    }

    override fun onSurveysUpdated() {
        items = cpxResearch.surveys
        notifyDataSetChanged()
    }

    override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) { }

    override fun onSurveysDidOpen( ) { }

    override fun onSurveysDidClose() { }

    override fun onSurveyDidOpen() { }

    override fun onSurveyDidClose() { }
}