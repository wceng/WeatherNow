package cn.wceng.weathernow.data.source.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import cn.wceng.weathernow.domain.model.City
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "cities",
    indices = [
        Index(value = ["id"])
    ]
)
data class LocalCity(
    @PrimaryKey
    val id: String,
    val name: String,
    val latitude: String,
    val longitude: String,
    val district: String,
    val province: String,
    val country: String,
    val weatherLink: String,
    val timestamp: Instant = Clock.System.now()
)

fun City.asLocalEntity() = LocalCity(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    district = district,
    province = province,
    country = country,
    weatherLink = weatherLink,
)