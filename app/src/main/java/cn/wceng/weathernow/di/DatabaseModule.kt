package cn.wceng.weathernow.di

import android.content.Context
import androidx.room.Room
import cn.wceng.weathernow.data.source.local.WeatherDatabase
import cn.wceng.weathernow.data.source.local.dao.CityDao
import cn.wceng.weathernow.data.source.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context,
    ): WeatherDatabase =
        Room
            .databaseBuilder(
                context,
                WeatherDatabase::class.java,
                "weather.db",
            )
            .build()

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()

    @Provides
    fun provideCityDao(database: WeatherDatabase): CityDao = database.cityDao()
}
