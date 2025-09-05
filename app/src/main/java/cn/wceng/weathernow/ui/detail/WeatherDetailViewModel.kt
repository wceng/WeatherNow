package cn.wceng.weathernow.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cn.wceng.weathernow.data.repository.WeatherRepository
import cn.wceng.weathernow.domain.model.CityWithWeather
import cn.wceng.weathernow.domain.usecase.GetCityWithWeatherUseCase
import cn.wceng.weathernow.ui.home.NestedWeatherDetailScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LOCAL_KEY = "local_key"

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCityWithWeatherUseCase: GetCityWithWeatherUseCase,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _cityId = savedStateHandle.toRoute<NestedWeatherDetailScreenRoute>().cityId
    private val cityIdStateFlow = savedStateHandle.getStateFlow(LOCAL_KEY, _cityId)

    private val selectedCity = cityIdStateFlow
        .flatMapLatest { cityId ->
            getCityWithWeatherUseCase(cityId)
        }

    private val _refreshing = MutableStateFlow(false)
    var userMessage: String? by mutableStateOf(null)

    val uiState: StateFlow<WeatherDetailUiState> = combine(
        _refreshing,
        selectedCity
    ) { refreshing, selectedCity ->
        WeatherDetailUiState.Success(
            cityWithWeather = selectedCity,
            refreshing = refreshing
        )
    }
        .onStart<WeatherDetailUiState> {
            emit(WeatherDetailUiState.Loading)
        }
        .catch {
            emit(WeatherDetailUiState.Error)
            it.printStackTrace()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            WeatherDetailUiState.Loading
        )

    fun refreshWeather() {
        viewModelScope.launch {
            _refreshing.value = true
            weatherRepository.refreshWeather(_cityId)
                .onFailure {
                    userMessage = "刷新失败"
                }
            _refreshing.value = false
        }
    }
}

sealed interface WeatherDetailUiState {
    data object Loading : WeatherDetailUiState

    data object EmptyCity : WeatherDetailUiState

    data class Success(
        val cityWithWeather: CityWithWeather,
        val refreshing: Boolean,
    ) : WeatherDetailUiState {
        override val userMessage: String?
            get() = super.userMessage
    }

    data object Error : WeatherDetailUiState

    val userMessage: String? get() = null
}