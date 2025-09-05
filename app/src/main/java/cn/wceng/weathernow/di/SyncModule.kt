package cn.wceng.weathernow.di

import android.content.Context
import androidx.work.WorkManager
import cn.wceng.weathernow.domain.sync.WeatherSyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideWeatherSyncManager(
        workManager: WorkManager,
    ): WeatherSyncManager {
        return WeatherSyncManager(workManager)
    }
}