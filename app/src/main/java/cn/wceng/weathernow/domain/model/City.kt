package cn.wceng.weathernow.domain.model

import cn.wceng.weathernow.data.source.local.entity.LocalCity
import cn.wceng.weathernow.data.source.remote.dto.NetworkCity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class City(
    val id: String,
    val name: String,
    val latitude: String,
    val longitude: String,
    val district: String, // adm2
    val province: String, // adm1
    val country: String,
    val weatherLink: String,
    val timestamp: Instant = Clock.System.now()
)

fun NetworkCity.asExternalModel(): List<City> {
    return this.location.map {
        City(
            id = it.id,
            name = it.name,
            latitude = it.lat,
            longitude = it.lon,
            district = it.adm2,
            province = it.adm1,
            country = it.country,
            weatherLink = it.fxLink,
        )
    }
}

fun LocalCity.asExternal(): City {
    return City(
        id = this.id,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        district = this.district,
        province = this.province,
        country = this.country,
        weatherLink = this.weatherLink,
        timestamp = this.timestamp
    )
}
