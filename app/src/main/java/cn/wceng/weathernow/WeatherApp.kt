package cn.wceng.weathernow

import android.app.Application
import androidx.glance.appwidget.updateAll
import cn.wceng.weathernow.domain.sync.WeatherSyncManager
import cn.wceng.weathernow.widget.NowWeatherWidget
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WeatherApp : Application(){

    @Inject
    lateinit var weatherSyncManager: WeatherSyncManager

    override fun onCreate() {
        super.onCreate()

//        weatherSyncManager.schedulePeriodicSync()

        CoroutineScope(Dispatchers.IO).launch{
            NowWeatherWidget().updateAll(this@WeatherApp)
        }
    }
}
