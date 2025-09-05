package cn.wceng.weathernow.domain.usecase

import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.data.repository.WeatherRepository
import cn.wceng.weathernow.domain.model.CityWithWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCityWithWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(cityId: String): Flow<CityWithWeather> {
        return combine(
            cityRepository.getCityById(cityId),
            weatherRepository.getWeatherInfo(cityId),
            userDataRepository.userData.map { it.currentCityId == cityId },
            ::CityWithWeather
        )
    }
}
