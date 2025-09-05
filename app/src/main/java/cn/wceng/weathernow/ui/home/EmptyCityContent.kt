package cn.wceng.weathernow.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import cn.wceng.weathernow.ui.theme.WeatherNowTheme

@Composable
fun EmptyCityContent(
    onManualLocationClick: () -> Unit = {},
    onAddCityClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val annotatedString = buildAnnotatedString {
            append("没有选择城市，你可以")

            pushStringAnnotation(
                tag = "manual_location",
                annotation = "manual_location"
            )
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append("手动定位")
            }
            pop()

            append("或")

            pushStringAnnotation(
                tag = "add_city",
                annotation = "add_city"
            )
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append("添加")
            }
            pop()

            append("城市")
        }

        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            onClick = { offset ->
                annotatedString
                    .getStringAnnotations(offset, offset)
                    .firstOrNull()
                    ?.let { annotation ->
                        when (annotation.tag) {
                            "manual_location" -> onManualLocationClick()
                            "add_city" -> onAddCityClick()
                        }
                    }
            }
        )
    }
}


@Preview
@Composable
private fun EmptyCityContentPreview() {
    WeatherNowTheme {
        Surface {
            EmptyCityContent()
        }
    }
}