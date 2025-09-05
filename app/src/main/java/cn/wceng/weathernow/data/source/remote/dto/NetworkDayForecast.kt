package cn.wceng.weathernow.data.source.remote.dto

import androidx.annotation.Keep

@Keep
data class NetworkDayForecast(
    val code: String, // 状态码
    val updateTime: String, // 当前API的最近更新时间
    val fxLink: String, // 当前数据的响应式页面
    val daily: List<DailyForecast>,
    val refer: Refer? = null // 数据来源信息，可能为空
) {
    @Keep
    data class DailyForecast(
        val fxDate: String, // 预报日期
        val sunrise: String? = null, // 日出时间，可能为空
        val sunset: String? = null, // 日落时间，可能为空
        val moonrise: String? = null, // 当天月升时间，可能为空
        val moonset: String? = null, // 当天月落时间，可能为空
        val moonPhase: String, // 月相名称
        val moonPhaseIcon: String, // 月相图标代码
        val tempMax: String, // 预报当天最高温度
        val tempMin: String, // 预报当天最低温度
        val iconDay: String, // 预报白天天气状况的图标代码
        val textDay: String, // 预报白天天气状况文字描述
        val iconNight: String, // 预报夜间天气状况的图标代码
        val textNight: String, // 预报夜间天气状况文字描述
        val wind360Day: String, // 预报白天风向360角度
        val windDirDay: String, // 预报白天风向
        val windScaleDay: String, // 预报白天风力等级
        val windSpeedDay: String, // 预报白天风速，公里/小时
        val wind360Night: String, // 预报夜间风向360角度
        val windDirNight: String, // 预报夜间风向
        val windScaleNight: String, // 预报夜间风力等级
        val windSpeedNight: String, // 预报夜间风速，公里/小时
        val humidity: String, // 相对湿度，百分比数值
        val precip: String, // 预报当天总降水量，默认单位：毫米
        val pressure: String, // 大气压强，默认单位：百帕
        val vis: String, // 能见度，默认单位：公里
        val cloud: String? = null, // 云量，百分比数值，可能为空
        val uvIndex: String // 紫外线强度指数
    )

    @Keep
    data class Refer(
        val sources: List<String>? = null, // 原始数据来源，可能为空
        val license: List<String>? = null // 数据许可或版权声明，可能为空
    )
}