package com.makeopinion.cpxresearchdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeopinion.cpxresearchlib.models.TransactionItem

class TransactionAdapter(private var transactions: List<TransactionItem>,
                         private var onClickListener: View.OnClickListener) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_transaction_item, parent, false)
        v.setOnClickListener(onClickListener)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setData(newData: List<TransactionItem>) {
        transactions = newData
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(survey: TransactionItem) {
            itemView.tag = survey.transId
            val title = itemView.findViewById(R.id.tv_title) as TextView
            val subtitle = itemView.findViewById(R.id.tv_subtitle) as TextView
            val content = itemView.findViewById(R.id.tv_content) as TextView
            title.text = "Id: ${survey.transId}"
            subtitle.text = "Verdienst Publisher: ${survey.verdienstPublisher}"
            content.text = StringBuilder()
                    .appendLine("MessageId: ${survey.messageId}")
                    .appendLine("Type: ${survey.type}")
                    .appendLine("SubId1: ${survey.subId1}")
                    .appendLine("SubId2: ${survey.subId2}")
                    .appendLine("DateTime: ${survey.dateTime}")
                    .appendLine("Status: ${survey.status}")
                    .appendLine("SurveyId: ${survey.surveyId}")
                    .appendLine("IP: ${survey.ipAddr}")
                    .appendLine("Loi: ${survey.loi}")
                    .appendLine("PaidToUser: ${survey.isPaidToUser}")
                    .appendLine("PaidToUserDateTime: ${survey.isPaidToUserDateTime}")
                    .appendLine("PaidToUserType: ${survey.isPaidToUserType}")
                    .toString()
        }
    }
}