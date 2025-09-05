package cn.wceng.weathernow.data.source.local

import androidx.room.TypeConverter
import cn.wceng.weathernow.data.model.DayForecastInfo
import cn.wceng.weathernow.data.model.HourForecastInfo
import cn.wceng.weathernow.data.model.IndicesInfo
import cn.wceng.weathernow.data.model.WarningInfo
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

class DataConverter {
    // 配置 Json 实例，处理 Instant 类型
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        serializersModule = SerializersModule {
            contextual(InstantSerializer)
        }
    }

    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun toHourForecastList(value: String): List<HourForecastInfo> {
        return try {
            if (value.isBlank() || value == "[]") emptyList()
            else json.decodeFromString(value)
        } catch (e: SerializationException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromHourForecastList(list: List<HourForecastInfo>): String {
        return try {
            if (list.isEmpty()) "[]"
            else json.encodeToString(list)
        } catch (e: SerializationException) {
            "[]"
        } catch (e: Exception) {
            "[]"
        }
    }

    @TypeConverter
    fun toDayForecastList(value: String): List<DayForecastInfo> {
        return try {
            if (value.isBlank() || value == "[]") emptyList()
            else json.decodeFromString(value)
        } catch (e: SerializationException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromDayForecastList(list: List<DayForecastInfo>): String {
        return try {
            if (list.isEmpty()) "[]"
            else json.encodeToString(list)
        } catch (e: SerializationException) {
            "[]"
        } catch (e: Exception) {
            "[]"
        }
    }

    @TypeConverter
    fun toWarningInfoList(value: String): List<WarningInfo> {
        return try {
            if (value.isBlank() || value == "[]") emptyList()
            else json.decodeFromString(value)
        } catch (e: SerializationException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromWarningInfoList(list: List<WarningInfo>): String {
        return try {
            if (list.isEmpty()) "[]"
            else json.encodeToString(list)
        } catch (e: SerializationException) {
            "[]"
        } catch (e: Exception) {
            "[]"
        }
    }

    @TypeConverter
    fun toIndicesInfoList(value: String): List<IndicesInfo> {
        return try {
            if (value.isBlank() || value == "[]") emptyList()
            else json.decodeFromString(value)
        } catch (e: SerializationException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromIndicesInfoList(list: List<IndicesInfo>): String {
        return try {
            if (list.isEmpty()) "[]"
            else json.encodeToString(list)
        } catch (e: SerializationException) {
            "[]"
        } catch (e: Exception) {
            "[]"
        }
    }
}


object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}