package com.makeopinion.cpxresearchdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeopinion.cpxresearchlib.models.SurveyItem

class SurveyAdapter(private var surveys: List<SurveyItem>,
                    private var onClickListener: View.OnClickListener) : RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_survey_item, parent, false)
        v.setOnClickListener(onClickListener)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(surveys[position])
    }

    override fun getItemCount(): Int {
        return surveys.size
    }

    fun setData(newData: List<SurveyItem>) {
        surveys = newData
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(survey: SurveyItem) {
            itemView.tag = survey.id
            val title = itemView.findViewById(R.id.tv_title) as TextView
            val subtitle = itemView.findViewById(R.id.tv_subtitle) as TextView
            val content = itemView.findViewById(R.id.tv_content) as TextView
            title.text = "Id: ${survey.id}"
            subtitle.text = survey.payout
            content.text = StringBuilder()
                    .appendLine("Loi: ${survey.loi}")
                    .appendLine("ConversionRate: ${survey.conversionRate}")
                    .appendLine("Type: ${survey.type}")
                    .appendLine("Top: ${survey.top}")
                    .appendLine("Details: ${survey.details}")
                    .appendLine("EarnedAll: ${survey.earnedAll}")
                    .appendLine("Parameters: ${survey.additionalParameter}")
                    .toString()
        }
    }
}