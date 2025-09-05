package cn.wceng.weathernow.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import cn.wceng.weathernow.data.model.DayForecastInfo
import cn.wceng.weathernow.data.model.HourForecastInfo
import cn.wceng.weathernow.domain.model.CityWithWeather

@Composable
fun MediumLargeWidget(
    cityWithWeather: CityWithWeather,
//    onWidgetClicked: Action,
    showDays: Boolean,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = glanceModifier
            .background(GlanceTheme.colors.primaryContainer)
            .fillMaxSize()
//            .clickable(onWidgetClicked)
    ) {
        Spacer(GlanceModifier.height(8.dp))
        MediumWidgetHeader(cityWithWeather)
        Spacer(modifier = GlanceModifier.defaultWeight())
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = GlanceModifier.fillMaxWidth(),
        ) {
            CurrentConditionsContent(cityWithWeather)
            Spacer(modifier = GlanceModifier.defaultWeight())
            HoursList(hourForecasts = cityWithWeather.hourForecastInfos)
        }
        Spacer(GlanceModifier.height(16.dp))
        if (showDays) {
            DaysList(daysList = cityWithWeather.dayForecastInfos)
            Spacer(GlanceModifier.height(16.dp))
        }
    }
}

@Composable
private fun MediumWidgetHeader(
    cityWithWeather: CityWithWeather
) {
    Row(
        verticalAlignment = Alignment.Vertical.CenterVertically,
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            provider = ImageProvider(cityWithWeather.nowWeather.iconRes),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = cityWithWeather.city.name,
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 16.sp,
                )
            )
            Text(
                text = cityWithWeather.nowWeather.nowTempDesc,
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

@Composable
private fun CurrentConditionsContent(cityWithWeather: CityWithWeather) {
    Column {
        Text(
            text = cityWithWeather.nowWeather.nowTemp,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier
                .padding(horizontal = 16.dp),
        )
//        Row(
//            modifier = GlanceModifier
//                .padding(horizontal = 16.dp)
//        ) {
//            Text(
//                text = weather.maxTemperature.temperatureString(),
//                style = TextStyle(
//                    color = GlanceTheme.colors.onPrimaryContainer,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            )
//            Spacer(modifier = GlanceModifier.width(4.dp))
//            Text(
//                text = weather.lowestTemperature.temperatureString(),
//                style = TextStyle(
//                    color = GlanceTheme.colors.onPrimaryContainer,
//                    fontSize = 14.sp,
//                )
//            )
//        }
    }
}

@Composable
private fun HoursList(
    hourForecasts: List<HourForecastInfo>,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(modifier = modifier) {
        hourForecasts.take(4).forEach { hourForecast ->
            HourRow(hour = hourForecast)
            Spacer(GlanceModifier.padding(end = 8.dp))
        }
    }
}

@Composable
private fun HourRow(
    hour: HourForecastInfo,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = hour.celsiusTemp,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Image(
            provider = ImageProvider(hour.iconRes),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Text(
            text = hour.displayTime,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun DaysList(
    daysList: List<DayForecastInfo>,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            )
    ) {
        LazyColumn(
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
//                .background(ImageProvider(R.drawable.weather_inside_shape_widget))
        ) {
            itemsIndexed(daysList.drop(1).take(2)) { index, weatherDay ->
                DayRow(
                    day = weatherDay,
                )
            }
        }
    }
}

@Composable
private fun DayRow(
    day: DayForecastInfo,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = day.displayTime,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 18.sp,
            )
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(day.iconRes),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = day.maxCelsiusTemp,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = day.minCelsiusTemp,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}