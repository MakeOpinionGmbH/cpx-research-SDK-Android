package com.makeopinion.cpxresearchlib.models

import com.google.gson.annotations.SerializedName

data class SurveyModel(var status: String,
                       @SerializedName(value = "count_available_surveys") val availableSurveysCount: Int,
                       @SerializedName(value = "count_returned_surveys") val returnedSurveysCount: Int,
                       val transactions: Array<TransactionItem>?,
                       val surveys: Array<SurveyItem>?,
                       val text: SurveyTextItem?
                       )

data class SurveyTextItem(
    @SerializedName(value = "is_html") val isHtml: Boolean?,
    @SerializedName(value = "currency_name_plural") val currencyNamePlural: String,
    @SerializedName(value = "currency_name_singular") val currencyNameSingular: String,
    @SerializedName(value = "shortcurt_min") val shortCurtMin: String,
    @SerializedName(value = "headline_general") val headlineGeneral: String,
    @SerializedName(value = "headline_1_element_1") val headline1Element1: String,
    @SerializedName(value = "headline_2_element_1") val headline2Element1: String,
    @SerializedName(value = "headline_1_element_2") val headline1Element2: String,
    @SerializedName(value = "reload_1_short_text") val reload1ShortText: String,
    @SerializedName(value = "reload_1_short_time") val reload1ShortTime: Long,
    @SerializedName(value = "reload_2_short_text") val reload2ShortText: String,
    @SerializedName(value = "reload_2_short_time") val reload2ShortTime: Long,
    @SerializedName(value = "reload_3_short_text") val reload3ShortText: String,
    @SerializedName(value = "reload_3_short_time") val reload3ShortTime: Long
)

data class SurveyItem(
    val id: String,
    val loi: Int,
    val payout: String,
    @SerializedName(value = "conversion_rate") val conversionRate: String,
    @SerializedName(value = "statistics_rating_count") val statisticsRatingCount: Int,
    @SerializedName(value = "statistics_rating_avg") val statisticsRatingAvg: Int,
    val type: String,
    val top: Int,
    val details: Int?,
    @SerializedName(value = "earned_all") val earnedAll: Int?,
    @SerializedName(value = "additional_parameter") val additionalParameter: Map<String, String>?
)

data class TransactionItem(
    @SerializedName(value = "trans_id") val transId: String,
    @SerializedName(value = "message_id") val messageId: String,
    val type: String,
    @SerializedName(value = "verdienst_publisher") val verdienstPublisher: String,
    @SerializedName(value = "verdienst_user_local_money") val verdienstUserLocalMoney: String,
    @SerializedName(value = "subid_1") val subId1: String,
    @SerializedName(value = "subid_2") val subId2: String,
    @SerializedName(value = "datetime") val dateTime: String,
    val status: String,
    @SerializedName(value = "survey_id") val surveyId: String,
    @SerializedName(value = "ip") val ipAddr: String,
    val loi: String,
    @SerializedName(value = "is_paid_to_user") val isPaidToUser: String,
    @SerializedName(value = "is_paid_to_user_datetime") val isPaidToUserDateTime: String,
    @SerializedName(value = "is_paid_to_user_type") val isPaidToUserType: String
) {
    /**
     * Earnings of the Publisher
     */
    val earningPublisher: String
        get() = verdienstPublisher

    /**
     * Earnings of the User
     */
    val earningUser: String
        get() =  verdienstPublisher

    /**
     * Earnings of the publisher as double type
     */
    val earningPublisherAsDouble: Double?
        get() = verdienstPublisher.toDoubleOrNull()

    /**
     * Earnings of the user as double type
     */
    val earningUserAsDouble: Double?
        get() =  verdienstPublisher.toDoubleOrNull()
}
