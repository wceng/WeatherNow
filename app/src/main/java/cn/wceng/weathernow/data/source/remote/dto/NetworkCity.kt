package cn.wceng.weathernow.data.source.remote.dto

import androidx.annotation.Keep

@Keep
data class NetworkCity(
    val code: String,
    val location: List<Location>,
) {
    @Keep
    data class Location(
        val name: String,
        val id: String,
        val lat: String,
        val lon: String,
        val adm2: String,
        val adm1: String,
        val country: String,
        val fxLink: String,
    )
}
