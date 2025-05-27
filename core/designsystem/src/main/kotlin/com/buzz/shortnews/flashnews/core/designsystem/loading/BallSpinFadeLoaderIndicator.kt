package com.buzz.shortnews.flashnews.core.designsystem.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

enum class LinearAnimationType(val animDuration: Int, val circleDelay: Long) {
    CIRCULAR(500, 100L),
    SKIP_AND_REPEAT(250, 250L);
}

@Composable
fun BallSpinFadeLoaderIndicator(
    modifier: Modifier,
    color: Color = Color.White,
    linearAnimationType: LinearAnimationType = LinearAnimationType.CIRCULAR,
    radius: Float = 70f,
    ballCount: Int = 8,
    ballRadius: Float = 12f
) {

    val angleStep = 360f / ballCount

// ------------------------ scale animation ---------------------
    val animationValues = (1..ballCount).map { index ->
        var animatedValue: Float by remember { mutableFloatStateOf(0f) }
        LaunchedEffect(key1 = Unit) {

            when (linearAnimationType) {
                LinearAnimationType.CIRCULAR -> {
                    delay(linearAnimationType.circleDelay * index)
                }

                LinearAnimationType.SKIP_AND_REPEAT -> {
                    delay(linearAnimationType.circleDelay * index) // The constant value, here 250L, must be the same animation duration for this pattern to run
                }
            }

            animate(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = when (linearAnimationType) {
                        LinearAnimationType.CIRCULAR -> {
                            tween(durationMillis = linearAnimationType.animDuration)
                        }

                        LinearAnimationType.SKIP_AND_REPEAT -> {
                            tween(durationMillis = linearAnimationType.animDuration)
                        }
                    },
                    repeatMode = RepeatMode.Reverse,
                )
            ) { value, _ -> animatedValue = value }
        }

        animatedValue
    }


// ----------------------------- UI --------------------------
    Canvas(
        modifier = modifier
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        for (index in 0 until ballCount) {
            val angle = index * angleStep
            val x = center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
            drawCircle(
                color = color,
                radius = ballRadius * animationValues[index], // Apply the scale
                center = Offset(x, y)
            )
        }
    }
}

@Preview
@Composable
fun BallSpinFadeLoaderIndicatorPreview() {
    BallSpinFadeLoaderIndicator(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .background(color = Color.Black)
    )
}