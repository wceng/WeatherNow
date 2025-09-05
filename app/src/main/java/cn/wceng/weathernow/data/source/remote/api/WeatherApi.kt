package cn.wceng.weathernow.data.source.remote.api

import cn.wceng.weathernow.data.source.remote.dto.NetworkDayForecast
import cn.wceng.weathernow.data.source.remote.dto.NetworkHourForecast
import cn.wceng.weathernow.data.source.remote.dto.NetworkIndices
import cn.wceng.weathernow.data.source.remote.dto.NetworkNowWeather
import cn.wceng.weathernow.data.source.remote.dto.NetworkWarning
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v7/weather/now")
    suspend fun getNowWeather(
        @Query("location", encoded = true) location: String,
    ): NetworkNowWeather

    @GET("/v7/weather/24h")
    suspend fun getHourForecast(
        @Query("location", encoded = true) location: String,
    ): NetworkHourForecast


    @GET("/v7/weather/7d")
    suspend fun getDayForecast(
        @Query("location", encoded = true) location: String,
    ): NetworkDayForecast


    @GET("/v7/warning/now")
    suspend fun getWarning(
        @Query("location", encoded = true) location: String,
    ): NetworkWarning


    @GET("/v7/indices/1d")
    suspend fun getIndices(
        @Query("location", encoded = true) location: String,
        @Query("type", encoded = true) type: String = "0",
    ): NetworkIndices
}
