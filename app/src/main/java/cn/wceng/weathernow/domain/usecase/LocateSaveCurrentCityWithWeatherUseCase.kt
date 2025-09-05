package cn.wceng.weathernow.domain.usecase

import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.data.repository.WeatherRepository
import cn.wceng.weathernow.util.NativeLocationProvider
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LocateSaveCurrentCityWithWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val nativeLocationProvider: NativeLocationProvider,
    private val userDataRepository: UserDataRepository,
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            val locationResult = nativeLocationProvider.getCurrentLocation()
                .firstOrNull { it != NativeLocationProvider.LocationResult.Loading }
            when (locationResult) {
                is NativeLocationProvider.LocationResult.Error,
                NativeLocationProvider.LocationResult.Loading,
                null -> throw Exception("定位失败")

                is NativeLocationProvider.LocationResult.Success -> {
                    val (lat, long) = locationResult.location.latitude to locationResult.location.longitude
                    val city = cityRepository.searchCities("$long,$lat").firstOrNull()

                    city?.let {
                        cityRepository.insertCity(it)
                        userDataRepository.setCurrentCityId(it.id)
                        userDataRepository.setCitySaved(it.id, true)
                        weatherRepository.refreshWeather(it.id)
                    } ?: throw Exception("定位失败")
                }
            }
        }
    }

}