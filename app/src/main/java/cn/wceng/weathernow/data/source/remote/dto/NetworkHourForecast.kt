package cn.wceng.weathernow.data.source.remote.dto

import androidx.annotation.Keep

@Keep
data class NetworkHourForecast(
    val code: String, // 状态码
    val updateTime: String, // 当前API的最近更新时间
    val fxLink: String, // 当前数据的响应式页面
    val hourly: List<HourlyForecast>,
    val refer: Refer? = null // 数据来源信息，可能为空
) {
    @Keep
    data class HourlyForecast(
        val fxTime: String, // 预报时间
        val temp: String, // 温度，默认单位：摄氏度
        val icon: String, // 天气状况的图标代码
        val text: String, // 天气状况的文字描述
        val wind360: String, // 风向360角度
        val windDir: String, // 风向
        val windScale: String, // 风力等级
        val windSpeed: String, // 风速，公里/小时
        val humidity: String, // 相对湿度，百分比数值
        val pop: String? = null, // 逐小时预报降水概率，可能为空
        val precip: String, // 当前小时累计降水量，默认单位：毫米
        val pressure: String, // 大气压强，默认单位：百帕
        val cloud: String? = null, // 云量，百分比数值，可能为空
        val dew: String? = null // 露点温度，可能为空
    )

    @Keep
    data class Refer(
        val sources: List<String>? = null, // 原始数据来源，可能为空
        val license: List<String>? = null // 数据许可或版权声明，可能为空
    )
}