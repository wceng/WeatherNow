package cn.wceng.weathernow.di

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import cn.wceng.weathernow.domain.usecase.GetSelectedCityWithWeatherUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getSelectedCityWithWeatherUseCase(): GetSelectedCityWithWeatherUseCase
}

object WidgetInjector {
    fun getSelectedCityWithWeatherUseCase(context: Context): GetSelectedCityWithWeatherUseCase {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        return entryPoint.getSelectedCityWithWeatherUseCase()
    }
}