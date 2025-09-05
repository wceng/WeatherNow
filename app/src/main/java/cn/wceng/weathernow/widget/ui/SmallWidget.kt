package cn.wceng.weathernow.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import cn.wceng.weathernow.domain.model.CityWithWeather

private const val MAX_WIDTH = 186

@Composable
fun SmallWidget(
    cityWithWeather: CityWithWeather,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    val nowWeather = cityWithWeather.nowWeather

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = glanceModifier
            .background(GlanceTheme.colors.primaryContainer)
            .size(width = MAX_WIDTH.dp, height = 184.dp)
//            .clickable(onWidgetClicked)
    ) {
        Spacer(GlanceModifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = GlanceModifier
                .width(width = MAX_WIDTH.dp)
                .padding(horizontal = 16.dp)
        ) {
            Image(
                provider = ImageProvider(nowWeather.iconRes),
                contentDescription = null,
                modifier = GlanceModifier.size(36.dp)
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = cityWithWeather.city.name,
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 16.sp,
                )
            )
        }
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = nowWeather.nowTemp,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier
                .width(width = MAX_WIDTH.dp)
                .padding(horizontal = 16.dp),
        )
//        Row(
//            modifier = GlanceModifier
//                .width(width = MAX_WIDTH.dp)
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
        Spacer(GlanceModifier.height(8.dp))
    }
}
