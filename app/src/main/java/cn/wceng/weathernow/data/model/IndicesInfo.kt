package cn.wceng.weathernow.data.model

import androidx.annotation.Keep
import cn.wceng.weathernow.data.source.remote.dto.NetworkIndices
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.serialization.Serializable

@Serializable
data class IndicesInfo (
    val date: Instant,
    val type: String,
    val typeName: String,
    val level: String,
    val category: String,
    val description: String
)

fun NetworkIndices.asExternalInfo() = this.daily.map {
    IndicesInfo(
        date = convertDateToInstant(it.date),
        type = it.type,
        typeName = it.name,
        level = it.level,
        category = it.category,
        description = it.text
    )
}

private fun convertDateToInstant(fxDate: String): Instant {
    return LocalDate.parse(fxDate).atStartOfDayIn(TimeZone.of("+08:00"))
}

