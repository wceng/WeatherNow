package cn.wceng.weathernow.data.model

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import cn.wceng.weathernow.data.source.remote.dto.NetworkHourForecast
import cn.wceng.weathernow.util.WeatherIconMapper
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class HourForecastInfo constructor(
    val fxTime: Instant, // 预报时间
    val temp: Int, // 温度，默认单位：摄氏度
    val icon: String, // 天气状况的图标代码
    val text: String, // 天气状况的文字描述
    val wind360: String, // 风向360角度
    val windDir: String, // 风向
) {
    val iconRes: Int @DrawableRes get() = WeatherIconMapper.getWeatherIconResource(icon)
    val displayTime: String get() = formatToHour(fxTime)
    val celsiusTemp: String get() = "$temp°"
}

@SuppressLint("DefaultLocale")
private fun formatToHour(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return String.format("%02d:%02d", localDateTime.hour, 0) // 将分钟部分固定为00
}

fun NetworkHourForecast.asExternalInfo(): List<HourForecastInfo> = this.hourly.map { hourly ->
    HourForecastInfo(
        fxTime = hourly.fxTime.toInstant(), // 转换为 Instant
        temp = hourly.temp.toIntOrNull() ?: 0, // 温度值转为 Int
        icon = hourly.icon, // 图标代码
        text = hourly.text, // 天气描述
        wind360 = hourly.wind360, // 风向360角度
        windDir = hourly.windDir // 风向描述
    )
}