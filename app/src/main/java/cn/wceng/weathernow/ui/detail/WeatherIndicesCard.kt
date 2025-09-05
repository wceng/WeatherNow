package cn.wceng.weathernow.ui.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wceng.weathernow.data.model.IndicesInfo
import cn.wceng.weathernow.ui.previewIndicesInfos

@Composable
fun WeatherIndicesCard(indicesInfos: List<IndicesInfo>) {
    var expanded by remember { mutableStateOf(false) }
    val visibleIndices = if (expanded) indicesInfos else indicesInfos.take(3)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "生活指数",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (expanded) "收起" else "展开全部",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }

            Spacer(Modifier.height(16.dp))

            // 指数列表
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                visibleIndices.forEach {
                    key(it) {
                        WeatherIndexItem(indexInfo = it)
                    }
                }
            }

            // 显示更多/更少提示
            if (indicesInfos.size > 3) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = if (expanded) "共${indicesInfos.size}项指数"
                    else "还有${indicesInfos.size - 3}项指数...",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun WeatherIndexItem(indexInfo: IndicesInfo) {
    val levelColor = getIndexLevelColor(indexInfo.level)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 指数图标和等级
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = levelColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = getIndexIcon(indexInfo.type),
                    style = MaterialTheme.typography.bodyLarge,
                    color = levelColor
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = indexInfo.category,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = levelColor,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }

        // 指数详情
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = indexInfo.typeName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Text(
                text = indexInfo.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = TextUnit(18f, TextUnitType.Sp)
                ),
                lineHeight = 18.sp
            )

            // 指数等级标签
            if (indexInfo.level.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = levelColor.copy(alpha = 0.1f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "等级 ${indexInfo.level}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = levelColor
                        )
                    )
                }
            }
        }
    }
}

// 辅助函数：根据指数等级获取颜色
@Composable
private fun getIndexLevelColor(level: String): Color {
    return when (level) {
        "1" -> Color(0xFF4CAF50) // 优 - 绿色
        "2" -> Color(0xFF8BC34A) // 良 - 浅绿色
        "3" -> Color(0xFFFFC107) // 中 - 黄色
        "4" -> Color(0xFFFF9800) // 较差 - 橙色
        "5" -> Color(0xFFF44336) // 差 - 红色
        else -> MaterialTheme.colorScheme.primary
    }
}

private fun getIndexIcon(type: String): String {
    return when (type) {
        "1" -> "🏃"  // 运动指数 - SPT
        "2" -> "🚗"  // 洗车指数 - CW
        "3" -> "👕"  // 穿衣指数 - DRSG
        "4" -> "🎣"  // 钓鱼指数 - FIS
        "5" -> "☀️"  // 紫外线指数 - UV
        "6" -> "✈️"  // 旅游指数 - TRA
        "7" -> "🤧"  // 花粉过敏指数 - AG
        "8" -> "😊"  // 舒适度指数 - COMF
        "9" -> "🤒"  // 感冒指数 - FLU
        "10" -> "🌫️" // 空气污染扩散条件指数 - AP
        "11" -> "❄️" // 空调开启指数 - AC
        "12" -> "🕶️" // 太阳镜指数 - GL
        "13" -> "💄" // 化妆指数 - MU
        "14" -> "☀️" // 晾晒指数 - DC
        "15" -> "🚦" // 交通指数 - PTFC
        "16" -> "🧴" // 防晒指数 - SPI
        else -> "📊"  // 默认图标
    }
}

@Preview
@Composable
private fun WeatherIndicesCardPreview() {
    Surface {
        WeatherIndicesCard(previewIndicesInfos)
    }
}