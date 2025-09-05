/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.wceng.weathernow.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import cn.wceng.weathernow.di.WidgetInjector
import cn.wceng.weathernow.domain.usecase.SelectedCityWithWeatherState
import cn.wceng.weathernow.widget.ui.MediumLargeWidget
import cn.wceng.weathernow.widget.ui.SmallWidget

class NowWeatherWidget : GlanceAppWidget() {

    companion object {
        private val smallMode = DpSize(260.dp, 184.dp)
        private val mediumMode = DpSize(270.dp, 200.dp)
        private val largeMode = DpSize(270.dp, 280.dp)
    }

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(
            setOf(smallMode, mediumMode, largeMode)
        )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val useCase = WidgetInjector.getSelectedCityWithWeatherUseCase(context)

        provideContent {
            val cityWithWeather by useCase().collectAsState(
                SelectedCityWithWeatherState.EmptyCity
            )

            GlanceTheme {
                HandleWidgetContent(cityWithWeather)
            }
        }
    }

    @GlanceComposable
    @Composable
    private fun HandleWidgetContent(cityWithWeatherState: SelectedCityWithWeatherState) {
        val size = LocalSize.current

        when (cityWithWeatherState) {
            SelectedCityWithWeatherState.EmptyCity -> Unit
            is SelectedCityWithWeatherState.Success -> {
                when (size) {
                    smallMode ->
                        SmallWidget(cityWithWeatherState.cityWithWeather)

                    mediumMode,largeMode ->
                        MediumLargeWidget(
                            cityWithWeather = cityWithWeatherState.cityWithWeather,
//                            onWidgetClicked = Action,
                            showDays = true,
                        )
                }
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
    }
}


