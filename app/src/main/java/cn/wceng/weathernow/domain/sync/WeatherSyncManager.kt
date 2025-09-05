package cn.wceng.weathernow.domain.sync

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherSyncManager @Inject constructor(
    private val workManager: WorkManager
) {

    fun schedulePeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<WeatherSyncWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "weather_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}