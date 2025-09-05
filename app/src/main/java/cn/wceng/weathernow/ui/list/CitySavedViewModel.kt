package cn.wceng.weathernow.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.domain.model.CityWithWeather
import cn.wceng.weathernow.domain.usecase.GetSavedCityWithWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CitySavedViewModel @Inject constructor(
    getSavedCityWithWeatherUseCase: GetSavedCityWithWeatherUseCase,
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<CitySavedUiState> =
        combine(
            userDataRepository.userData,
            getSavedCityWithWeatherUseCase()
        ) { userData, savedCityWithWeathers ->
            if (savedCityWithWeathers.isEmpty()) CitySavedUiState.Empty
            else CitySavedUiState.Success(
                cityWithWeathers = savedCityWithWeathers,
            )
        }
            .catch { emit(CitySavedUiState.Error(it.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CitySavedUiState.Loading
            )
}

sealed interface CitySavedUiState {
    data object Loading : CitySavedUiState
    data class Success(
        val cityWithWeathers: List<CityWithWeather>
    ) : CitySavedUiState

    data object Empty : CitySavedUiState
    data class Error(val message: String?) : CitySavedUiState
}