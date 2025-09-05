package cn.wceng.weathernow.domain.model

import cn.wceng.weathernow.data.model.WeatherInfo

data class CityWithWeather constructor(
    val city: City,
    val weatherInfo: WeatherInfo?,
    val isCurrentCity: Boolean = false
) {
    val nowWeather
        get() = if (weatherInfo != null) NowWeather(city, weatherInfo) else NowWeather(city)

    val hourForecastInfos get() = weatherInfo?.hourForecastInfos ?: emptyList()

    val dayForecastInfos get() = weatherInfo?.dayForecastInfos ?: emptyList()

    val warningInfos get() = weatherInfo?.warningInfos ?: emptyList()

    val indicesInfos get() = weatherInfo?.indicesInfos ?: emptyList()
}