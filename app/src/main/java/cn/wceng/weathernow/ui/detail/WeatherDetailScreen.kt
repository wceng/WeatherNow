package cn.wceng.weathernow.ui.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.wceng.weathernow.data.model.DayForecastInfo
import cn.wceng.weathernow.data.model.HourForecastInfo
import cn.wceng.weathernow.domain.model.NowWeather
import cn.wceng.weathernow.ui.component.ErrorContent
import cn.wceng.weathernow.ui.component.HorizontalDividerLow
import cn.wceng.weathernow.ui.component.LoadingContent
import cn.wceng.weathernow.ui.component.ProportionalTemperatureBar
import cn.wceng.weathernow.ui.component.WeatherNowCard
import cn.wceng.weathernow.ui.previewCityWithWeather
import cn.wceng.weathernow.ui.previewDayForecastInfos
import cn.wceng.weathernow.ui.previewHourForecastInfos
import cn.wceng.weathernow.ui.previewNowWeather
import cn.wceng.weathernow.ui.theme.WeatherNowTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

private const val TAG = "WeatherDetailScreen"

@OptIn(ExperimentalPermissionsApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun WeatherDetailScreen(
    onShowSnackBar: (String) -> Unit,
    viewModel: WeatherDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeatherDetailContent(
        uiState = uiState,
        onSwipeRefresh = viewModel::refreshWeather,
    )

    LaunchedEffect(viewModel.userMessage) {
        viewModel.userMessage?.let {
            onShowSnackBar(it)
            viewModel.userMessage = null
        }
    }
}

@Composable
private fun WeatherDetailContent(
    uiState: WeatherDetailUiState,
    refreshState: PullToRefreshState = rememberPullToRefreshState(),
    onSwipeRefresh: () -> Unit = {},
) {

    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                WeatherDetailUiState.Error -> ErrorContent()
                WeatherDetailUiState.Loading -> LoadingContent()
                is WeatherDetailUiState.Success -> {
                    PullToRefreshBox(
                        state = refreshState,
                        isRefreshing = uiState.refreshing,
                        onRefresh = onSwipeRefresh,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                        ) {
                            Spacer(Modifier)

                            if (uiState.cityWithWeather.warningInfos.isNotEmpty()) {
                                WarningCard(uiState.cityWithWeather.warningInfos)
                            }

                            NowWeatherCard(uiState.cityWithWeather.nowWeather)

                            if (uiState.cityWithWeather.hourForecastInfos.isNotEmpty()) {
                                HourForecastCard(uiState.cityWithWeather.hourForecastInfos)
                            }


                            if (uiState.cityWithWeather.dayForecastInfos.isNotEmpty()) {
                                DayForecastCard(uiState.cityWithWeather.dayForecastInfos)
                            }

                            if (uiState.cityWithWeather.indicesInfos.isNotEmpty()) {
                                WeatherIndicesCard(uiState.cityWithWeather.indicesInfos)
                            }

                            Spacer(Modifier.height(48.dp))
                        }
                    }
                }

                WeatherDetailUiState.EmptyCity -> Unit
            }
        }
    }
}

@Composable
private fun NowWeatherCard(
    nowWeather: NowWeather
) {
    WeatherNowCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nowWeather.location,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = nowWeather.nowTemp,
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = nowWeather.feelsLike,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(nowWeather.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.height(12.dp))

                Text(
                    text = nowWeather.nowTempDesc,
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "更新时间：" + nowWeather.lastUpdateTimeText,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun NowWeatherInfoPreview() {
    WeatherNowTheme {
        Surface {
            NowWeatherCard(
                nowWeather = previewNowWeather
            )
        }
    }
}

@Composable
private fun HourForecastCard(
    hourForecasts: List<HourForecastInfo>
) {
    WeatherNowCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "24小时预报",
                style = MaterialTheme.typography.headlineSmall,
            )

            HorizontalDividerLow()

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(items = hourForecasts, key = { hourForecasts.indexOf(it) }) {
                    HourForecastItem(
                        hourForecast = it
                    )
                }
            }
        }
    }
}

@Composable
private fun HourForecastItem(
    modifier: Modifier = Modifier,
    hourForecast: HourForecastInfo
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(text = hourForecast.displayTime, style = MaterialTheme.typography.labelLarge)
        Icon(
            painter = painterResource(hourForecast.iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(text = hourForecast.celsiusTemp, style = MaterialTheme.typography.labelMedium)
    }
}

@Preview
@Composable
private fun HourForecastCardPreview() {
    WeatherNowTheme {
        Surface {
            HourForecastCard(
                hourForecasts = previewHourForecastInfos
            )
        }
    }
}

@Composable
private fun DayForecastCard(
    dayForecasts: List<DayForecastInfo>,
) {
    val maxWeekTemp by remember(dayForecasts) {
        mutableIntStateOf(
            dayForecasts.maxOfOrNull { it.tempMax } ?: 40
        )
    }

    val minWeekTemp by remember(dayForecasts) {
        mutableIntStateOf(
            dayForecasts.minOfOrNull { it.tempMin } ?: -40
        )
    }

    WeatherNowCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "7日预报",
                style = MaterialTheme.typography.headlineSmall,
            )
            HorizontalDividerLow()
            dayForecasts.forEach {
                key(it.displayTime) {
                    DayForecastItem(it, maxWeekTemp, minWeekTemp)
                    HorizontalDividerLow()
                }
            }
        }
    }
}

@Composable
private fun DayForecastItem(
    dayForecast: DayForecastInfo,
    maxWeekTemp: Int,
    minWeekTemp: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Column(modifier = Modifier.weight(0.4f)) {
            Text(
                text = dayForecast.displayTime,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = dayForecast.textDay,
            )
        }

        ProportionalTemperatureBar(
            modifier = Modifier.weight(1f),
            globalMin = minWeekTemp,
            globalMax = maxWeekTemp,
            itemMin = dayForecast.tempMin,
            itemMax = dayForecast.tempMax,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(dayForecast.iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(4.dp))
            Column {
                Text(text = dayForecast.minCelsiusTemp)
                Text(text = dayForecast.maxCelsiusTemp)
            }
        }
    }
}


@Preview
@Composable
private fun DayForecastItemPreview() {
    WeatherNowTheme {
        Surface {
            DayForecastItem(
                previewDayForecastInfos.first(),
                40,
                10
            )
        }
    }
}

@Preview
@Composable
private fun DayForecastCardPreview() {
    WeatherNowTheme {
        Surface {
            DayForecastCard(dayForecasts = previewDayForecastInfos)
        }
    }
}

@Preview
@Composable
private fun WeatherDetailContentPreview() {
    WeatherNowTheme {
        Surface {
            WeatherDetailContent(
                WeatherDetailUiState.Success(
                    previewCityWithWeather,
                    refreshing = true
                ),
                onSwipeRefresh = {},
            )
        }
    }
}