package cn.wceng.weathernow.domain.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.data.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val weatherRepository: WeatherRepository,
    private val cityRepository: CityRepository,
    private val userDataRepository: UserDataRepository
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: WeatherRepository

    override suspend fun doWork(): Result {
        return try {
            // 1. 获取需要同步的城市ID列表
            val cityIds = getSavedCityIds()

            // 2. 并行刷新所有城市数据
//            cityIds.map { cityId ->
//                async { repository.refreshWeather(cityId) }
//            }.awaitAll()

            Result.success()
        } catch (e: Exception) {
            Result.retry() // 失败时自动重试
        }
    }

    private suspend fun getSavedCityIds(): List<String> {
        // 从数据库或Preferences获取收藏城市ID
        return emptyList() // 实际实现替换为真实数据
    }
}