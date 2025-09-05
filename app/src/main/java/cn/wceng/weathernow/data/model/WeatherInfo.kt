package cn.wceng.weathernow.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import cn.wceng.weathernow.data.source.local.entity.LocalWeather
import cn.wceng.weathernow.data.source.remote.dto.NetworkDayForecast
import cn.wceng.weathernow.data.source.remote.dto.NetworkHourForecast
import cn.wceng.weathernow.data.source.remote.dto.NetworkIndices
import cn.wceng.weathernow.data.source.remote.dto.NetworkNowWeather
import cn.wceng.weathernow.data.source.remote.dto.NetworkWarning
import cn.wceng.weathernow.util.WeatherIconMapper
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant


data class WeatherInfo constructor(
    val location: String,
    val updateTime: Instant,
    val obsTime: Instant,
    val temp: Int,
    val feelsLike: Int,
    val icon: String,
    val text: String,
    val windDir: String,
    val windScale: String,
    val humidity: String,
    val hourForecastInfos: List<HourForecastInfo>,
    val dayForecastInfos: List<DayForecastInfo>,
    val warningInfos: List<WarningInfo>,
    val indicesInfos: List<IndicesInfo>
) {

    constructor(
        location: String,
        networkNowWeather: NetworkNowWeather,
        networkHourForecast: NetworkHourForecast,
        networkDayForecast: NetworkDayForecast,
        networkWarning: NetworkWarning,
        networkIndices: NetworkIndices
    ) : this(
        location = location,
        updateTime = networkNowWeather.updateTime.toInstant(),
        obsTime = networkNowWeather.now.obsTime.toInstant(),
        temp = networkNowWeather.now.temp.toIntOrNull() ?: 0,
        feelsLike = networkNowWeather.now.feelsLike.toIntOrNull() ?: 0,
        icon = networkNowWeather.now.icon,
        text = networkNowWeather.now.text,
        windDir = networkNowWeather.now.windDir,
        windScale = networkNowWeather.now.windScale,
        humidity = networkNowWeather.now.humidity,
        hourForecastInfos = networkHourForecast.asExternalInfo(),
        dayForecastInfos = networkDayForecast.asExternalInfo(),
        warningInfos = networkWarning.asExternalInfo(),
        indicesInfos = networkIndices.asExternalInfo()
    )

    val iconRes: Int @DrawableRes get() = WeatherIconMapper.getWeatherIconResource(icon)
}

fun LocalWeather.asExternalInfo() = WeatherInfo(
    location = location,
    updateTime = updateTime,
    obsTime = obsTime,
    temp = temp,
    feelsLike = feelsLike,
    icon = icon,
    text = text,
    windDir = windDir,
    windScale = windScale,
    humidity = humidity,
    hourForecastInfos = hourForecasts,
    dayForecastInfos = dayForecasts,
    warningInfos = warnings,
    indicesInfos = indices
)

