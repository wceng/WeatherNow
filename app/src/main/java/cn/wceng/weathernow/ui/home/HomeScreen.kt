@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalPermissionsApi::class)

package cn.wceng.weathernow.ui.home

import android.Manifest
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.wceng.weathernow.ui.component.LoadingContent
import cn.wceng.weathernow.ui.detail.WeatherDetailScreen
import cn.wceng.weathernow.ui.list.SavedCitiesScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(WindowInsets.safeDrawing),
            uiState = homeScreenUiState,
            onNavigateToSearch = onNavigateToSearch,
            onStartLocate = viewModel::startLocate,
            onShowUserMessage = viewModel::showUserMessage,
            onSelectCity = viewModel::selectCity,
            onDeleteCity = viewModel::deleteCity
        )
    }

    LaunchedEffect(viewModel.userMessage) {
        viewModel.userMessage?.let {
            if (snackbarHostState.showSnackbar(
                    message = it, duration = SnackbarDuration.Short
                ) == SnackbarResult.Dismissed
            ) {
                viewModel.userMessageShown()
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onNavigateToSearch: () -> Unit,
    onStartLocate: () -> Unit,
    onShowUserMessage: (String) -> Unit,
    onSelectCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val permissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        onPermissionsResult = {
            if (it.all { it.value }) {
                onStartLocate()
            } else {
                onShowUserMessage("权限被拒绝")
            }
        })

    Box(modifier = modifier) {
        when (uiState) {
            HomeScreenUiState.EmptyCity -> EmptyCityPlaceHolderContent(
                allPermissionGranted = permissionState.allPermissionsGranted && !permissionState.shouldShowRationale,
                onRequestPermission = {
                    permissionState.launchMultiplePermissionRequest()
                },
                onNavigateToSearch = onNavigateToSearch,
                onStartLocate = onStartLocate
            )

            HomeScreenUiState.Loading -> LoadingContent()
            is HomeScreenUiState.Success -> {
                HomeContentScaffold(
                    selectableCityWithWeathersModel = uiState.selectableCityWithWeathersModel,
                    onNavigateToSearch = onNavigateToSearch,
                    onSelectCity = onSelectCity,
                    onDeleteCity = onDeleteCity,
                    onShowUserMessage = onShowUserMessage
                )
            }
        }
    }
}

@Composable
private fun HomeContentScaffold(
    selectableCityWithWeathersModel: SelectableCityWithWeathersModel,
    onNavigateToSearch: () -> Unit,
    onSelectCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
) {
    LaunchedEffect(selectableCityWithWeathersModel) {
//        println(
//            "AllPageNumber:${selectableCityWithWeathersModel.allCityWithWeathers.size}" +
//                    " SelectedCityId:${selectableCityWithWeathersModel.selectedCity?.city?.id}" +
//                    ""
//        )
    }

    val initialPage by remember {
        mutableIntStateOf(selectableCityWithWeathersModel.currentPageIndex)
    }


    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = initialPage) {
        selectableCityWithWeathersModel.pageCount
    }

    val listDetailPaneScaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        initialDestinationHistory = listOf(
            ThreePaneScaffoldDestinationItem<Any>(ListDetailPaneScaffoldRole.Detail)
        )
    )

    Scaffold(
        floatingActionButton = {
            BottomWeatherNavigationBar(
                showListButton = listDetailPaneScaffoldNavigator.isDetailPaneVisible(),
                onNavigateToCitySavedScreen = {
                    scope.launch {
                        listDetailPaneScaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.List)
                    }
                },
                onNavigateToSearchScreen = onNavigateToSearch,
                onNavigatePreviousCity = {
                    scope.launch {
                        selectableCityWithWeathersModel.getCityByIndex(pagerState.currentPage - 1)
                            ?.let {
                                onSelectCity(it.city.id)
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                    }
                },
                onNavigateNextCity = {
                    scope.launch {
                        selectableCityWithWeathersModel.getCityByIndex(pagerState.currentPage + 1)
                            ?.let {
                                onSelectCity(it.city.id)
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                    }
                },
                onDeleteCurrentCity = {
                    scope.launch {
                        selectableCityWithWeathersModel.selectedCity?.let {
                            onDeleteCity(it.city.id)
                            pagerState.animateScrollToPage(0)
                        }
                    }

                },
                canScrollPrevious = pagerState.canScrollBackward,
                canScrollNext = pagerState.canScrollForward,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        HomeContentListDetailPane(
            modifier = Modifier.padding(paddingValues),
            onShowUserMessage = onShowUserMessage,
            selectableCityWithWeathersModel = selectableCityWithWeathersModel,
            onSelectCity = onSelectCity,
            pagerState = pagerState,
            listDetailNavigator = listDetailPaneScaffoldNavigator
        )
    }
}

@Composable
private fun HomeContentListDetailPane(
    modifier: Modifier = Modifier,
    selectableCityWithWeathersModel: SelectableCityWithWeathersModel,
    onShowUserMessage: (String) -> Unit,
    onSelectCity: (String) -> Unit,
    listDetailNavigator: ThreePaneScaffoldNavigator<Any>,
    pagerState: PagerState = rememberPagerState { 0 },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    LaunchedEffect(selectableCityWithWeathersModel) {
        pagerState.interactionSource.interactions.collect {
            when (it) {
                is DragInteraction.Start -> {

                }

                is DragInteraction.Stop -> {
                    delay(100)
                    onSelectCity(
                        selectableCityWithWeathersModel.allCityWithWeathers[pagerState.targetPage].city.id
                    )
                    println("User gesture scroll to page index: ${pagerState.targetPage}")
                }

                is DragInteraction.Cancel -> {

                }
            }
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = listDetailNavigator,
        listPane = {
            AnimatedPane {
                SavedCitiesScreen(
                    onSelectCity = {
                        coroutineScope.launch {
                            val pageIndex =
                                selectableCityWithWeathersModel.allCityWithWeathers.map { it.city.id }
                                    .indexOf(it)

                            pagerState.animateScrollToPage(pageIndex)

                            if (listDetailNavigator.canNavigateBack()) {
                                listDetailNavigator.navigateBack()
                            }
                        }
                        onSelectCity(it)

                    },
                    selectedCityId = selectableCityWithWeathersModel.selectedCity?.city?.id
                )
            }
        },
        detailPane = {
            AnimatedPane {
                HorizontalPager(
                    state = pagerState,
                    key = {
                        selectableCityWithWeathersModel.allCityWithWeathers
                            .getOrNull(it)?.city?.id ?: ""
                    }
                ) { pagerIndex ->
                    val currentCity =
                        selectableCityWithWeathersModel.allCityWithWeathers[pagerIndex]

                    LaunchedEffect(currentCity) {
//                        println("HorizontalPager: Page current index: $pagerIndex city id: ${currentCity.city.id}")
                    }
                    NestedNavigateWeatherDetailScreen(
                        cityId = currentCity.city.id,
                        onShowSnackBar = onShowUserMessage,
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
private fun EmptyCityPlaceHolderContent(
    allPermissionGranted: Boolean,
    onRequestPermission: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onStartLocate: () -> Unit
) {
    var showRequestLocationPermissionDialog by remember { mutableStateOf(false) }

    EmptyCityContent(
        onManualLocationClick = {
            if (allPermissionGranted) {
                onStartLocate()
            } else {
                showRequestLocationPermissionDialog = true
            }
        }, onAddCityClick = onNavigateToSearch
    )

    if (showRequestLocationPermissionDialog) {
        RequestLocationPermissionDialog(
            onDismissRequest = { showRequestLocationPermissionDialog = false },
            onConfirm = {
                onRequestPermission()
                showRequestLocationPermissionDialog = false
            },
        )
    }
}

@Serializable
data class NestedWeatherDetailScreenRoute(
    val cityId: String
)

@Composable
private fun NestedNavigateWeatherDetailScreen(
    cityId: String,
    onShowSnackBar: (String) -> Unit,
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = NestedWeatherDetailScreenRoute(cityId),
    ) {
        composable<NestedWeatherDetailScreenRoute> {
            WeatherDetailScreen(
                onShowSnackBar = onShowSnackBar,
            )
        }
    }
}

@Composable
private fun BottomWeatherNavigationBar(
    showListButton: Boolean,
    onNavigateToCitySavedScreen: () -> Unit,
    onNavigateToSearchScreen: () -> Unit,
    onNavigatePreviousCity: () -> Unit,
    onNavigateNextCity: () -> Unit,
    onDeleteCurrentCity: () -> Unit,
    canScrollPrevious: Boolean,
    canScrollNext: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Row {
            if (showListButton) IconButton(onClick = onNavigateToCitySavedScreen) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
            }

            IconButton(onClick = onNavigatePreviousCity, enabled = canScrollPrevious) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            IconButton(onClick = onNavigateNextCity, enabled = canScrollNext) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null
                )
            }

            IconButton(onClick = onDeleteCurrentCity) {
                Icon(
                    imageVector = Icons.Default.Delete, contentDescription = null
                )
            }

            IconButton(onClick = onNavigateToSearchScreen) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    }
}


//@OptIn(ExperimentalMaterial3AdaptiveApi::class)
//private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
//    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

