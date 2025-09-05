@file:OptIn(ExperimentalPermissionsApi::class)

package cn.wceng.weathernow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import cn.wceng.weathernow.ui.home.HomeScreen
import cn.wceng.weathernow.ui.search.CitySearchScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.serialization.Serializable

@Composable
fun WeatherNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
//    val navOptions = navOptions {
//        // Pop up to the start destination of the graph to
//        // avoid building up a large stack of destinations
//        // on the back stack as users select items
//        popUpTo(navController.graph.findStartDestination().id) {
//            saveState = true
//        }
//        // Avoid multiple copies of the same destination when
//        // reselecting the same item
//        launchSingleTop = true
//        // Restore state when reselecting a previously selected item
//        restoreState = true
//    }

    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute::class,
        modifier = modifier,
    ) {
        composable<HomeScreenRoute> {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(CitySearchRoute)
                },
            )
        }

        composable<CitySearchRoute> {
            CitySearchScreen(
                onNavigateToHomeScreen = {
                    navController.navigate(HomeScreenRoute(it), navOptions {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    })
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

    }
}

@Serializable
data class HomeScreenRoute(
    val selectedCityId: String? = null
)

@Serializable
data object CitySearchRoute

