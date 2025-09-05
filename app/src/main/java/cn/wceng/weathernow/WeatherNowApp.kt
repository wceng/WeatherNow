package cn.wceng.weathernow

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import cn.wceng.weathernow.navigation.WeatherNavGraph

@Composable
fun WeatherNowApp() {
    Surface {
        WeatherNavGraph()
    }
}