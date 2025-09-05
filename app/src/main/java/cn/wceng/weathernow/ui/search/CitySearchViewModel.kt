@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package cn.wceng.weathernow.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.domain.model.SavableCity
import cn.wceng.weathernow.domain.usecase.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_QUERY_KEY = "city_search_query"

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val cachedSearchResult = mutableMapOf<String, SavableCity>()

    val searchQuery = savedStateHandle.getStateFlow(
        key = SEARCH_QUERY_KEY,
        initialValue = ""
    )

    val uiState: StateFlow<CitySearchUiState> =
        searchQuery
            .map { it.trim { it.isWhitespace() } }
            .distinctUntilChanged()
            .debounce(300)
            .flatMapLatest { query ->
                when {
                    query.isEmpty() -> flowOf(CitySearchUiState.Idle) // 清空时返回初始状态
//                    query.length < 2 -> flowOf(CitySearchUiState.Idle) // 不足2字符也返回初始状态
                    else -> searchCityUseCase(query)
                        .onEach {
                            cachedSearchResult.putAll(it.associateBy { it.city.id })
                        }
                        .map {
                            if (it.isNotEmpty())
                                CitySearchUiState.Success(it)
                            else
                                CitySearchUiState.EmptyResult
                        }
                }
            }
            .catch { emit(CitySearchUiState.Error(it.message ?: "未知错误")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CitySearchUiState.Idle,
            )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }

    fun savedCity(cityId: String, saved: Boolean) {
        viewModelScope.launch {
            if (saved) {
                cachedSearchResult[cityId]?.let {
                    cityRepository.insertCity(it.city)
                }
            }
            userDataRepository.setCitySaved(cityId, saved)
        }
    }
}

sealed interface CitySearchUiState {
    data object Idle : CitySearchUiState
    data object Loading : CitySearchUiState
    data class Success(
        val savedCities: List<SavableCity>,
    ) : CitySearchUiState

    data class Error(
        val message: String,
    ) : CitySearchUiState

    data object EmptyResult : CitySearchUiState
}