package cn.wceng.weathernow.data.source.remote.api

import cn.wceng.weathernow.BuildConfig
import cn.wceng.weathernow.data.source.remote.dto.NetworkCity
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("/geo/v2/city/lookup")
    suspend fun searchCities(
        @Query("location", encoded = true) query: String,
    ): NetworkCity
}
