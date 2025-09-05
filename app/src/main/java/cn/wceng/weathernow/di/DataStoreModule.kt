package cn.wceng.weathernow.di

import android.content.Context
import cn.wceng.weathernow.data.source.datastore.UserPreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserPreferencesManager(
        @ApplicationContext context: Context,
    ): UserPreferencesDataSource = UserPreferencesDataSource(context)


}
