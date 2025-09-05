package cn.wceng.weathernow.domain.usecase

import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.data.repository.WeatherRepository
import cn.wceng.weathernow.domain.model.CityWithWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetSavedCityWithWeatherUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(): Flow<List<CityWithWeather>> {
        return userDataRepository.userData
            .flatMapLatest { userData ->
                combine(
                    cityRepository.getCitiesByIds(userData.savedCityIds),
                    weatherRepository.getWeatherInfos(userData.savedCityIds)
                ) { cities, weathers ->
                    cities.map { city ->
                        CityWithWeather(
                            city = city,
                            weatherInfo = weathers.firstOrNull { it.location == city.id },
                            isCurrentCity = city.id == userData.currentCityId
                        )
                    }
                        .sortedWith(
                            compareByDescending<CityWithWeather> { it.isCurrentCity }
                                .thenBy { it.city.timestamp }
                        )
                }
            }
    }

}
