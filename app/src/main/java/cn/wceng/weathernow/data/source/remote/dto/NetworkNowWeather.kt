package cn.wceng.weathernow.data.source.remote.dto

import androidx.annotation.Keep

@Keep
data class NetworkNowWeather(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val now: Now,
) {
    @Keep
    data class Now(
        val obsTime: String,
        val temp: String,
        val feelsLike: String,
        val icon: String,
        val text: String,
        val windDir: String,
        val windScale: String,
        val humidity: String,
    )
}
