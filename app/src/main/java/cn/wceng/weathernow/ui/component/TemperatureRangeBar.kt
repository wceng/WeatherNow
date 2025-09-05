package cn.wceng.weathernow.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 多日天气预报温度条组件
 * @param modifier 用于控制布局的修饰符
 * @param globalMin 全局最低温
 * @param globalMax 全局最高温
 * @param itemMin 当前item最低温
 * @param itemMax 当前item最高温
 */
@Composable
fun ProportionalTemperatureBar(
    globalMin: Int,
    globalMax: Int,
    itemMin: Int,
    itemMax: Int,
    modifier: Modifier = Modifier,
) {
    require(globalMax > globalMin) { "全局最高温必须大于最低温" }
    require(itemMax >= itemMin) { "当前item最高温必须大于等于最低温" }
    require(itemMin >= globalMin && itemMax <= globalMax) { "当前item温度必须在全局范围内" }

    val barHeight = 6.dp

    // 计算比例位置（0f~1f）
    val startRatio = remember(itemMin, globalMin, globalMax) {
        ((itemMin - globalMin).toFloat() / (globalMax - globalMin)).coerceIn(0f, 1f)
    }
    val endRatio = remember(itemMax, globalMin, globalMax) {
        ((itemMax - globalMin).toFloat() / (globalMax - globalMin)).coerceIn(0f, 1f)
    }

    // 温度颜色计算
    val startColor = getColorForTemperature(itemMin)
    val endColor = getColorForTemperature(itemMax)

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .height(barHeight)
                .fillMaxWidth()
        ) {
            val totalWidth = size.width
            val cornerRadius = barHeight.toPx() / 2 // 半圆角效果

            // 1. 绘制全局背景（灰色底）
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.1f),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(cornerRadius)
            )

            // 2. 绘制当前item的温度条（按比例）
            val itemStartX = startRatio * totalWidth
            val itemEndX = endRatio * totalWidth
            val itemWidth = itemEndX - itemStartX

            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(startColor, endColor)
                ),
                topLeft = Offset(itemStartX, 0f),
                size = size.copy(width = itemWidth),
                cornerRadius = CornerRadius(cornerRadius)
            )
        }
    }
}

// 温度颜色映射函数
private fun getColorForTemperature(temp: Int): Color {
    return when {
        temp <= -20 -> Color(0xFF00008B)
        temp <= -10 -> Color(0xFF0000CD)
        temp <= 0 -> Color(0xFF1E90FF)
        temp <= 10 -> Color(0xFF00BFFF)
        temp <= 15 -> Color(0xFF3CB371)
        temp <= 20 -> Color(0xFF7CFC00)
        temp <= 25 -> Color(0xFFFFFF00)
        temp <= 30 -> Color(0xFFFFA500)
        temp <= 35 -> Color(0xFFFF8C00)
        else -> Color(0xFFFF4500)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProportionalTemperatureBarPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 标题文字
        Text(
            text = "温度条比例演示",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 场景1：正常范围（全局 20~36°C，当前 24~32°C）
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("场景1:", modifier = Modifier.width(60.dp))
            ProportionalTemperatureBar(
                globalMin = 20,
                globalMax = 36,
                itemMin = 24,
                itemMax = 32,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("24-32°C")
        }

        // 场景2：低温范围（全局 -5~25°C，当前 2~18°C）
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("场景2:", modifier = Modifier.width(60.dp))
            ProportionalTemperatureBar(
                globalMin = -5,
                globalMax = 25,
                itemMin = 2,
                itemMax = 18,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("2-18°C")
        }

        // 场景3：窄范围（全局 28~38°C，当前 30~32°C）
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("场景3:", modifier = Modifier.width(60.dp))
            ProportionalTemperatureBar(
                globalMin = 28,
                globalMax = 38,
                itemMin = 30,
                itemMax = 32,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("30-32°C")
        }

        // 场景4：极端范围（全局 -10~42°C，当前 5~35°C）
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("场景4:", modifier = Modifier.width(60.dp))
            ProportionalTemperatureBar(
                globalMin = -10,
                globalMax = 42,
                itemMin = 5,
                itemMax = 42,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("5-35°C")
        }

        // 场景5：最小宽度演示
        ProportionalTemperatureBar(
            globalMin = 0,
            globalMax = 30,
            itemMin = 10,
            itemMax = 20,
        )
    }
}