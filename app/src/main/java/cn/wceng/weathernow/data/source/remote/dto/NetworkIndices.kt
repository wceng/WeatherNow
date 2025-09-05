package cn.wceng.weathernow.data.source.remote.dto

import androidx.annotation.Keep

@Keep
data class NetworkIndices(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val daily: List<DailyIndex>,
    val refer: Refer
) {
    @Keep
    data class DailyIndex(
        val date: String,
        val type: String,
        val name: String,
        val level: String,
        val category: String,
        val text: String
    )

    @Keep
    data class Refer(
        val sources: List<String>,
        val license: List<String>
    )
}