package cn.wceng.weathernow.data.repository

import cn.wceng.weathernow.data.model.WeatherInfo
import cn.wceng.weathernow.data.model.asExternalInfo
import cn.wceng.weathernow.data.source.local.dao.WeatherDao
import cn.wceng.weathernow.data.source.local.entity.asLocalEntity
import cn.wceng.weathernow.data.source.remote.api.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface WeatherRepository {
    fun getWeatherInfo(location: String): Flow<WeatherInfo?>

    fun getWeatherInfos(locations: Set<String>): Flow<List<WeatherInfo>>

    suspend fun refreshWeather(location: String): Result<Unit>
}

class OfflineFirstWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
) : WeatherRepository {

    override fun getWeatherInfo(location: String): Flow<WeatherInfo?> =
        weatherDao.getWeatherStream(location)
            .map { it?.asExternalInfo() }

    override fun getWeatherInfos(locations: Set<String>): Flow<List<WeatherInfo>> {
        return weatherDao.getWeathersStream(locations)
            .map { locals ->
                locals.map {
                    it.asExternalInfo()
                }
            }
    }

    override suspend fun refreshWeather(location: String): Result<Unit> {
        return runCatching {
            val nowWeather = weatherApi.getNowWeather(location)
            val hourForecast = weatherApi.getHourForecast(location)
            val dayForecast = weatherApi.getDayForecast(location)
            val warning = weatherApi.getWarning(location)
            val indices = weatherApi.getIndices(location)

            val weatherInfo = WeatherInfo(
                location = location,
                networkNowWeather = nowWeather,
                networkHourForecast = hourForecast,
                networkDayForecast = dayForecast,
                networkWarning = warning,
                networkIndices = indices
            )
            weatherDao.insertWeather(weatherInfo.asLocalEntity())
        }
    }
}
