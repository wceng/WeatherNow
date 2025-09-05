package cn.wceng.weathernow.di

import cn.wceng.weathernow.data.source.datastore.UserPreferencesDataSource
import cn.wceng.weathernow.data.source.local.dao.CityDao
import cn.wceng.weathernow.data.source.local.dao.WeatherDao
import cn.wceng.weathernow.data.source.remote.api.CityApi
import cn.wceng.weathernow.data.source.remote.api.WeatherApi
import cn.wceng.weathernow.data.repository.OfflineFirstCityRepository
import cn.wceng.weathernow.data.repository.OfflineFirstUserDataRepository
import cn.wceng.weathernow.data.repository.OfflineFirstWeatherRepository
import cn.wceng.weathernow.data.repository.CityRepository
import cn.wceng.weathernow.data.repository.UserDataRepository
import cn.wceng.weathernow.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApi: WeatherApi,
        weatherDao: WeatherDao,
    ): WeatherRepository = OfflineFirstWeatherRepository(weatherApi, weatherDao)

    @Provides
    @Singleton
    fun provideCityRepository(
        api: CityApi,
        dao: CityDao,
    ): CityRepository = OfflineFirstCityRepository(api, dao)

    @Provides
    @Singleton
    fun provideUserDataRepository(manager: UserPreferencesDataSource): UserDataRepository =
        OfflineFirstUserDataRepository(manager)
}
