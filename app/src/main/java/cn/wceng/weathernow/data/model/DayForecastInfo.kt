package cn.wceng.weathernow.data.model

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import cn.wceng.weathernow.data.source.remote.dto.NetworkDayForecast
import cn.wceng.weathernow.util.WeatherIconMapper
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DayForecastInfo(
    val fxDate: Instant, // 预报日期
    val tempMax: Int, // 预报当天最高温度
    val tempMin: Int, // 预报当天最低温度
    val iconDay: String, // 预报白天天气状况的图标代码
    val textDay: String, // 预报白天天气状况文字描述
    val iconNight: String, // 预报夜间天气状况的图标代码
    val textNight: String, // 预报夜间天气状况文字描述
) {
    val iconRes: Int @DrawableRes get() = WeatherIconMapper.getWeatherIconResource(iconDay)
    val displayTime: String get() = convertToWeekDay(fxDate)
    val minCelsiusTemp: String get() = "${tempMin}°"
    val maxCelsiusTemp: String get() = "${tempMax}°"
}

private fun convertToWeekDay(instant: Instant): String {
    val targetDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val today = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    return when (targetDate) {
        today -> "今天"
        today.plus(1, DateTimeUnit.DAY) -> "明天"
        else -> getWeekDayName(targetDate)
    }
}

@SuppressLint("NewApi")
private fun getWeekDayName(date: LocalDate): String {
    return when (date.dayOfWeek) {
        kotlinx.datetime.DayOfWeek.MONDAY -> "周一"
        kotlinx.datetime.DayOfWeek.TUESDAY -> "周二"
        kotlinx.datetime.DayOfWeek.WEDNESDAY -> "周三"
        kotlinx.datetime.DayOfWeek.THURSDAY -> "周四"
        kotlinx.datetime.DayOfWeek.FRIDAY -> "周五"
        kotlinx.datetime.DayOfWeek.SATURDAY -> "周六"
        kotlinx.datetime.DayOfWeek.SUNDAY -> "周日"
    }
}

fun NetworkDayForecast.asExternalInfo(): List<DayForecastInfo> {
    return this.daily.map { daily ->
        DayForecastInfo(
            fxDate = convertDayFxDateToInstant(daily.fxDate), // 转换日期字符串为Instant
            tempMax = daily.tempMax.toIntOrNull() ?: 0, // 最高温度转为Int
            tempMin = daily.tempMin.toIntOrNull() ?: 0, // 最低温度转为Int
            iconDay = daily.iconDay, // 白天图标代码
            textDay = daily.textDay, // 白天天气描述
            iconNight = daily.iconNight, // 夜间图标代码
            textNight = daily.textNight // 夜间天气描述
        )
    }
}

private fun convertDayFxDateToInstant(fxDate: String): Instant {
    return LocalDate.parse(fxDate).atStartOfDayIn(TimeZone.of("+08:00"))
}

