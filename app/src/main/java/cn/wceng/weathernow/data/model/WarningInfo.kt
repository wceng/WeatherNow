package cn.wceng.weathernow.data.model

import androidx.annotation.Keep
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cn.wceng.weathernow.data.source.remote.dto.NetworkWarning
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
data class WarningInfo(
    val sender: String,
    val pubTime: Instant,
    val title: String,
    val startTime: Instant,
    val endTime: Instant,
    val status: String,
    val severity: String,
    val severityColor: String,
    val type: String,
    val typeName: String,
    val urgency: String,
    val certainty: String,
    val text: String,
) {
    val severityLevel: Int
        get() = when (severity.lowercase()) {
            "cancel" -> -1
            "none" -> 0
            "unknown" -> 1
            "standard" -> 2
            "minor" -> 3
            "moderate" -> 4
            "major" -> 5
            "severe" -> 6
            "extreme" -> 7
            else -> 1
        }

    val severityDisplayText: String
        get() = when (severity.lowercase()) {
            "cancel" -> "已取消"
            "none" -> "无预警"
            "unknown" -> "未知等级"
            "standard" -> "标准"
            "minor" -> "轻微"
            "moderate" -> "中等"
            "major" -> "主要"
            "severe" -> "严重"
            "extreme" -> "极端"
            else -> severity
        }


    val severityColorValue: Color
        @Composable get() {
            return when (severityColor.lowercase()) {
                "black" -> Color(0xFF000000)      // 黑色 - 最高级别
                "red" -> Color(0xFFD32F2F)        // 红色 - 严重
                "orange" -> Color(0xFFFF9800)     // 橙色 - 较重
                "yellow" -> Color(0xFFFFEB3B)     // 黄色 - 中等
                "green" -> Color(0xFF4CAF50)      // 绿色 - 一般
                "blue" -> Color(0xFF2196F3)       // 蓝色 - 轻微
                "white" -> Color(0xFFFFFFFF)      // 白色 - 最低级别
                else -> MaterialTheme.colorScheme.primary
            }
        }

    val formattedPubTime: String
        get() {
            val formatter = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
            return formatter.format(pubTime.toEpochMilliseconds())
        }

    val formattedTimeRange: String
        get() {
            val formatter = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
            return "${formatter.format(startTime.toEpochMilliseconds())} - ${
                formatter.format(
                    endTime.toEpochMilliseconds()
                )
            }"
        }

    val isActive: Boolean
        get() = status.equals("active", ignoreCase = true)
}

fun NetworkWarning.asExternalInfo() = this.warning.map {
    WarningInfo(
        sender = it.sender,
        pubTime = it.pubTime.toInstant(),
        title = it.title,
        startTime = it.startTime.toInstant(),
        endTime = it.endTime.toInstant(),
        status = it.status,
        severity = it.severity,
        severityColor = it.severityColor,
        type = it.type,
        typeName = it.typeName,
        urgency = it.urgency,
        certainty = it.certainty,
        text = it.text
    )
}
