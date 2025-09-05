package cn.wceng.weathernow.data.repository

import android.util.Log
import cn.wceng.weathernow.data.source.local.dao.CityDao
import cn.wceng.weathernow.data.source.local.entity.asLocalEntity
import cn.wceng.weathernow.data.source.remote.api.CityApi
import cn.wceng.weathernow.domain.model.City
import cn.wceng.weathernow.domain.model.asExternal
import cn.wceng.weathernow.domain.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG: String = "OfflineFirstCityRepository"

interface CityRepository {
    suspend fun searchCities(query: String): List<City>

    fun getCitiesByIds(ids: Set<String>): Flow<List<City>>

    fun getCityById(cityId: String): Flow<City>

    suspend fun insertCity(city: City)

    suspend fun deleteCity(cityId: String)
}

class OfflineFirstCityRepository @Inject constructor(
    private val api: CityApi,
    private val cityDao: CityDao,
) : CityRepository {

    override suspend fun searchCities(query: String): List<City> {
        val response = api.searchCities(query)
        return if (response.code == "200") {
            response.asExternalModel()
        } else {
            Log.w(TAG, "searchCities: response fail, code is ${response.code}")
            emptyList()
        }
    }

    override fun getCitiesByIds(ids: Set<String>): Flow<List<City>> =
        cityDao.getCitiesByIds(ids)
            .map { local ->
                local.map {
                    it.asExternal()
                }
            }

    override fun getCityById(cityId: String): Flow<City> {
        return cityDao.getCityById(cityId).map { it.asExternal() }
    }

    override suspend fun insertCity(city: City) {
        cityDao.insertCity(city.asLocalEntity())
    }

    override suspend fun deleteCity(cityId: String) {
        cityDao.deleteCity(cityId)
    }
}
