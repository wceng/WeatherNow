package cn.wceng.weathernow.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import cn.wceng.weathernow.R
import cn.wceng.weathernow.data.model.WeatherInfo

@Immutable
data class NowWeather(
    val nowTemp: String, //实时温度  32（华氏度）
    val nowTempDesc: String, //晴
    val location: String, //比如 江苏,吴中，苏州
    val feelsLike: String,
    @DrawableRes
    val iconRes: Int,
) {
    constructor(
        city: City,
        weatherInfo: WeatherInfo
    ) : this(
        nowTemp = "${weatherInfo.temp}°", // 摄氏度转华氏度
        nowTempDesc = weatherInfo.text, // 天气描述
        location = formatLocation(city), // 格式化地理位置
        feelsLike = "体感 ${weatherInfo.feelsLike}°", // 体感温度
        iconRes = weatherInfo.iconRes // 直接使用Weather中的iconRes
    )

    constructor(
        city: City,
    ) : this(
        nowTemp = "--°",
        nowTempDesc = "--",
        location = formatLocation(city),
        feelsLike = "体感 --°",
        iconRes = R.drawable.ic_weather_999
    )
}

// 格式化地理位置辅助函数
private fun formatLocation(city: City): String {
    return listOf(city.name, city.district, city.province, city.country)
        .distinct()
        .joinToString(",")
}