package cn.wceng.weathernow.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cn.wceng.weathernow.data.model.DayForecastInfo
import cn.wceng.weathernow.data.model.HourForecastInfo
import cn.wceng.weathernow.data.model.IndicesInfo
import cn.wceng.weathernow.data.model.WarningInfo
import cn.wceng.weathernow.data.model.WeatherInfo
import kotlinx.datetime.Instant

@Entity(
    tableName = "weather",
    indices = [
        Index(value = ["location"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocalCity::class,
            parentColumns = ["id"],
            childColumns = ["location"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class LocalWeather(
    @PrimaryKey
    val location: String,
    val updateTime: Instant,
    val obsTime: Instant,
    val temp: Int,
    val feelsLike: Int,
    val icon: String,
    val text: String,
    val windDir: String,
    val windScale: String,
    val humidity: String,
    @ColumnInfo(name = "last_fetched")
    val lastFetched: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "hour_forecasts")
    val hourForecasts: List<HourForecastInfo>,
    @ColumnInfo(name = "day_forecasts")
    val dayForecasts: List<DayForecastInfo>,
    @ColumnInfo(name = "warnings")
    val warnings: List<WarningInfo>,
    @ColumnInfo(name = "indices")
    val indices: List<IndicesInfo>
)

fun WeatherInfo.asLocalEntity() =
    LocalWeather(
        location = location,
        updateTime = updateTime,
        obsTime = obsTime,
        temp = temp,
        feelsLike = feelsLike,
        icon = icon,
        text = text,
        windDir = windDir,
        windScale = windScale,
        humidity = humidity,
        hourForecasts = hourForecastInfos,
        dayForecasts = dayForecastInfos,
        warnings = warningInfos,
        indices = indicesInfos
    )
