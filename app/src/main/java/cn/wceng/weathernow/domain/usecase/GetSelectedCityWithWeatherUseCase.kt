package cn.wceng.weathernow.domain.usecase

import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.domain.model.CityWithWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSelectedCityWithWeatherUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val getCityWithWeatherUseCase: GetCityWithWeatherUseCase
) {
    operator fun invoke(): Flow<SelectedCityWithWeatherState> {
        return userDataRepository.userData
            .map { it.currentCityId }
            .flatMapLatest { cityId ->
                cityId?.let {
                    getCityWithWeatherUseCase(cityId)
                        .map(SelectedCityWithWeatherState::Success)
                } ?: flowOf(SelectedCityWithWeatherState.EmptyCity)
            }
    }
}

sealed interface SelectedCityWithWeatherState {
    data object EmptyCity : SelectedCityWithWeatherState

    data class Success(
        val cityWithWeather: CityWithWeather
    ) : SelectedCityWithWeatherState
}