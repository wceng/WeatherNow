package cn.wceng.weathernow.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.domain.model.CityWithWeather
import cn.wceng.weathernow.domain.usecase.GetSavedCityWithWeatherUseCase
import cn.wceng.weathernow.domain.usecase.LocateSaveCurrentCityWithWeatherUseCase
import cn.wceng.weathernow.navigation.HomeScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SelectedCityKey = "SelectedCityKey"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    getSavedCityWithWeatherUseCase: GetSavedCityWithWeatherUseCase,
    private val locateSaveCurrentCityWithWeatherUseCase: LocateSaveCurrentCityWithWeatherUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val routeSelectedCityId = savedStateHandle.toRoute<HomeScreenRoute>().selectedCityId

    private val selectedCityId: StateFlow<String?> =
        savedStateHandle.getStateFlow(SelectedCityKey, routeSelectedCityId)

    init {
        viewModelScope.launch {
            routeSelectedCityId?.let {
                savedStateHandle[SelectedCityKey] = it
            }
            println("HomeScreenViewModel initial cityId: $routeSelectedCityId")
        }
    }

    var userMessage by mutableStateOf<String?>(null)
        private set

    val homeScreenUiState: StateFlow<HomeScreenUiState> =
        combine(selectedCityId, getSavedCityWithWeatherUseCase()) { selectedCityId, cities ->
            when {
                cities.isEmpty() -> HomeScreenUiState.EmptyCity
                else -> {
                    val selectedCity = cities.firstOrNull { selectedCityId == it.city.id }
                    HomeScreenUiState.Success(
                        SelectableCityWithWeathersModel(
                            allCityWithWeathers = cities,
                            selectedCity = selectedCity ?: cities.firstOrNull()
                        )
                    )
                }
            }
        }
            .catch { it.printStackTrace() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                HomeScreenUiState.Loading
            )

    fun deleteCity(cityId: String) {
        viewModelScope.launch {
            userDataRepository.setCitySaved(cityId, false)
            if (userDataRepository.userData.map { it.currentCityId }.first() == cityId) {
                userDataRepository.setCurrentCityId(null)
            }
            println("HomeScreenViewModel: deleteCity:$cityId")
        }
    }

    fun startLocate() {
        viewModelScope.launch {
            locateSaveCurrentCityWithWeatherUseCase()
                .onFailure {
                    userMessage = it.message
                }
        }
    }


    fun showUserMessage(message: String) {
        userMessage = message
    }

    fun userMessageShown() {
        userMessage = null
    }

    fun selectCity(cityId: String) {
        savedStateHandle[SelectedCityKey] = cityId
        println("HomeScreenViewModel: selectCity: $cityId ")
    }
}

sealed interface HomeScreenUiState {
    data object EmptyCity : HomeScreenUiState

    data class Success(
        val selectableCityWithWeathersModel: SelectableCityWithWeathersModel
    ) : HomeScreenUiState

    data object Loading : HomeScreenUiState
}

data class SelectableCityWithWeathersModel(
    val allCityWithWeathers: List<CityWithWeather> = emptyList(),
    val selectedCity: CityWithWeather? = allCityWithWeathers.firstOrNull()
) {
    val pageCount: Int
        get() = allCityWithWeathers.size

    val currentPageIndex: Int
        get() = allCityWithWeathers.indexOf(selectedCity)

    fun getCityByIndex(index: Int) = allCityWithWeathers.getOrNull(index)
}