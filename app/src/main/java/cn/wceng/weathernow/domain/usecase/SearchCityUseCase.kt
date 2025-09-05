package cn.wceng.weathernow.domain.usecase

import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.domain.model.City
import cn.wceng.weathernow.domain.model.SavableCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val cityRepository: CityRepository
) {

    private val searchResultCached = mutableMapOf<String, List<City>>()

    operator fun invoke(query: String): Flow<List<SavableCity>> {
        return userDataRepository.userData
            .map { it.savedCityIds }
            .distinctUntilChanged()
            .map { savedCityIds ->
                val tempCities = mutableListOf<City>()

                if (searchResultCached.contains(query)) {
                    tempCities.addAll(searchResultCached[query] ?: listOf())
                } else {
                    val searchCities = cityRepository.searchCities(query)
                    searchResultCached.put(query, searchCities)
                    tempCities.addAll(searchCities)
                }

                tempCities.map { city ->
                    SavableCity(
                        city = city,
                        saved = city.id in savedCityIds
                    )
                }
            }
    }
}