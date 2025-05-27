package com.buzz.shortnews.flashnews.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.theme.CustomGameStyleFontFamily
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun StrokedGradientText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    strokeWidth: Dp = 2.dp,
    isAutoSize: Boolean = false,
    strokeColor: Color = Color(0xFFC42D22),
    shadowOffsetY: Dp = 2.dp,
    shadowColor: Color = Color(0xFFAD1A10),
    gradientColors: List<Color> = listOf(Color(0xFFFFF100), Color(0xFFFAD000))
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val containerWidthPx = remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .onSizeChanged {
                containerWidthPx.floatValue = it.width.toFloat()
            }
    ) {
        var realFontSize = fontSize
        val minFontSize = fontSize / 2
        var layoutResult: TextLayoutResult
        var textStyle = TextStyle(
            fontSize = realFontSize,
            fontWeight = fontWeight,
            fontFamily = CustomGameStyleFontFamily,
            textAlign = TextAlign.Center
        )
        layoutResult = textMeasurer.measure(AnnotatedString(text), textStyle)
        while (isAutoSize && layoutResult.size.width > containerWidthPx.floatValue) {
            if (realFontSize > minFontSize) {
                realFontSize = (realFontSize.value - 1).sp
            } else {
                break
            }
            textStyle = TextStyle(
                fontSize = realFontSize,
                fontWeight = fontWeight,
                fontFamily = CustomGameStyleFontFamily,
                textAlign = TextAlign.Center
            )
            layoutResult = textMeasurer.measure(AnnotatedString(text), textStyle)
        }

        val textHeightDp = with(density) {
            (layoutResult.size.height + 2 * strokeWidth.toPx() + shadowOffsetY.toPx()).toDp()
        }

        Canvas(
            modifier = Modifier
                .height(textHeightDp)
                .width(with(density) { layoutResult.size.width.toDp() })
        ) {
            val shadowOffsetPx = with(density) { shadowOffsetY.toPx() }
            val strokeWidthPx = with(density) { strokeWidth.toPx() }
            val centerX = size.width / 2 - layoutResult.size.width / 2
            val centerY = (size.height - layoutResult.size.height) / 2f

            // 1. 绘制阴影
            for (angle in 0 until 360 step 45) {
                val x = strokeWidthPx * cos(angle * Math.PI / 180).toFloat()
                drawText(
                    textLayoutResult = layoutResult,
                    color = shadowColor,
                    topLeft = Offset(centerX + x, centerY + shadowOffsetPx)
                )
            }

            // 2. 绘制描边
            for (angle in 0 until 360 step 45) {
                val x = strokeWidthPx * cos(angle * Math.PI / 180).toFloat()
                val y = strokeWidthPx * sin(angle * Math.PI / 180).toFloat()
                drawText(
                    textLayoutResult = layoutResult,
                    color = strokeColor,
                    topLeft = Offset(centerX + x, centerY + y)
                )
            }
            // 3. 绘制渐变填充
            drawText(
                textLayoutResult = layoutResult,
                brush = Brush.verticalGradient(gradientColors),
                topLeft = Offset(centerX, centerY)
            )
        }
    }
}
