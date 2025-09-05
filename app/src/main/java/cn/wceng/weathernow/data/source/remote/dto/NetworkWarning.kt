package cn.wceng.weathernow.data.source.remote.dto

import androidx.annotation.Keep

@Keep
data class NetworkWarning(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val warning: List<WarningItem>,
    val refer: Refer
) {
    @Keep
    data class WarningItem(
        val id: String,
        val sender: String,
        val pubTime: String,
        val title: String,
        val startTime: String,
        val endTime: String,
        val status: String,
        val level: String,
        val severity: String,
        val severityColor: String,
        val type: String,
        val typeName: String,
        val urgency: String,
        val certainty: String,
        val text: String,
        val related: String
    )

    @Keep
    data class Refer(
        val sources: List<String>,
        val license: List<String>
    )
}