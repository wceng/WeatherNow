package cn.wceng.weathernow.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.wceng.weathernow.domain.model.CityWithWeather
import cn.wceng.weathernow.ui.component.ErrorContent
import cn.wceng.weathernow.ui.component.LoadingContent
import cn.wceng.weathernow.ui.component.WeatherNowCheckableCard
import cn.wceng.weathernow.ui.previewCityWithWeather
import cn.wceng.weathernow.ui.previewCityWithWeathers
import cn.wceng.weathernow.ui.theme.WeatherNowTheme

@Composable
fun SavedCitiesScreen(
    onSelectCity: (cityId: String) -> Unit,
    selectedCityId: String? = null,
    viewModel: CitySavedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CitySavedContent(
        uiState = uiState,
        onCityClick = onSelectCity,
        selectedCityId = selectedCityId
    )
}

@Composable
fun CitySavedContent(
    uiState: CitySavedUiState,
    selectedCityId: String? = null,
    onCityClick: (cityId: String) -> Unit,
) {
    Box {
        when (uiState) {
            is CitySavedUiState.Loading -> {
                LoadingContent()
            }

            is CitySavedUiState.Success -> {
                CityList(
                    cityWithWeathers = uiState.cityWithWeathers,
                    onCityClick = onCityClick,
                    selectedCityId = selectedCityId
                )
            }

            CitySavedUiState.Empty -> Unit

            is CitySavedUiState.Error -> ErrorContent(message = uiState.message)
        }
    }
}

@Composable
private fun CityList(
    cityWithWeathers: List<CityWithWeather>,
    onCityClick: (cityId: String) -> Unit,
    modifier: Modifier = Modifier,
    selectedCityId: String? = null
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(Modifier)
        }

        items(cityWithWeathers, key = { it.city.id }) { city ->
            CityItem(
                cityWithWeather = city,
                onClick = { onCityClick(city.city.id) },
                checked = selectedCityId == city.city.id,
            )
        }

        item {
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun CityItem(
    checked: Boolean,
    cityWithWeather: CityWithWeather,
    onClick: () -> Unit,
) {
    WeatherNowCheckableCard(
        checked = checked,
        onCheckedChange = {
            onClick()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = cityWithWeather.nowWeather.nowTemp,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cityWithWeather.city.name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.width(16.dp))

                    if (cityWithWeather.isCurrentCity)
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )
                }
            }

            Icon(
                painter = painterResource(cityWithWeather.nowWeather.iconRes),
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityWithWeatherItemPreview() {
    WeatherNowTheme {
        CityItem(
            cityWithWeather = previewCityWithWeather,
            checked = true,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SavedCitiesEmptyPreview() {
    WeatherNowTheme {
        CitySavedContent(
            uiState = CitySavedUiState.Empty,
            onCityClick = {},
        )
    }
}


@Preview
@Composable
private fun CitySavedContentSuccess() {
    WeatherNowTheme {
        Surface {
            CitySavedContent(
                uiState = CitySavedUiState.Success(
                    cityWithWeathers = previewCityWithWeathers,
                ),
                onCityClick = {}
            )
        }
    }
}