package com.buzz.shortnews.flashnews.core.designsystem

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF00CE00
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF03B103
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF509A01
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF9DE602
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF9DE703
import com.buzz.shortnews.flashnews.core.ui.commonDrawable

/**
 * current 和 max的值需要在收到onReward回调之后刷新
 * TODO:Font的自定义暂时不支持
 */
@Composable
fun BoxRewardProgressBar(
    modifier: Modifier = Modifier,
    progressWidth: Dp = 150.dp,
    boxWidth: Dp = 46.dp,
    current: State<Int>,
    max: State<Int>,
    finishAllReward: State<Boolean>,
    doReward: () -> Unit,
) {
    val progressModifier: Modifier = Modifier
        .width(progressWidth)
        .height(21.dp)
    val boxModifier: Modifier = Modifier
        .width(boxWidth)
        .height(38.dp)

    val progress = remember {
        derivedStateOf { current.value / max.value.toFloat() }
    }
    val canReceive = remember {
        derivedStateOf { current.value >= max.value }
    }

    Box(
        modifier = modifier
            .padding(end = boxWidth / 2), contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = progressModifier
                .background(
                    color = Color(0xFFa32742), shape = RoundedCornerShape(999.dp)
                )
                .padding(2.dp)
                .clip(RoundedCornerShape(999.dp)) // 💡裁剪内部内容,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF790227))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    color_FF9DE703, color_FF9DE602, color_FF509A01
                                )
                            ),
                        )
                        .fillMaxWidth(fraction = progress.value),
                )
                // Text for progress count (e.g. 18/25)
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-1).dp),
                    text = "${current.value}/${max.value}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        lineHeight = 14.sp
                    )
                )

            }


        }

        Box(modifier = Modifier
            .offset(x = boxWidth / 2)
            .align(Alignment.CenterEnd)
            .clickable(enabled = canReceive.value && finishAllReward.value.not() && max.value > 0) {
                doReward()
            }) {
            RotateBox(
                modifier = boxModifier,
                boxWidth = boxWidth,
                canReceive = canReceive,
                finishAllReward = finishAllReward
            )
        }
    }
}

@Composable
private fun RotateBox(
    modifier: Modifier,
    boxWidth: Dp,
    canReceive: State<Boolean>,
    finishAllReward: State<Boolean>
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // 使用独立的动画控制器
    val shakeAnim = remember { Animatable(-10f) }

    // 启动无限动画（自动处理生命周期）
    LaunchedEffect(Unit) {
        while (true) {
            shakeAnim.animateTo(
                10f,
                animationSpec = tween(500, easing = LinearEasing)
            )
            shakeAnim.animateTo(
                -10f,
                animationSpec = tween(500, easing = LinearEasing)
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        if (canReceive.value && finishAllReward.value.not()) {
            Image(
                modifier = Modifier
                    .size(55.dp)
                    .graphicsLayer {
                        clip = false
                        rotationZ = rotationAnimation
                    },
                painter = painterResource(id = commonDrawable.read_news_task_light),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Image(
            painter = painterResource(id = commonDrawable.box_progress),
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .graphicsLayer {
                    // 直接读取动画值，不通过 State 触发重组
                    rotationZ =
                        if (canReceive.value && finishAllReward.value.not()) shakeAnim.value else 0f
                },
            colorFilter = if (finishAllReward.value) {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            } else null
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun BoxRewardProgressBarPreview() {
    val current = mutableIntStateOf(0)
    val max = mutableIntStateOf(0)
    val finishAllReward = mutableStateOf(false)
    BoxRewardProgressBar(current = current,
        max = max,
        finishAllReward = finishAllReward,
        doReward = {})
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun RotateBoxPreview() {
    val boxModifier = Modifier
        .width(46.dp)
        .height(38.dp)
    val boxWidth = 46.dp
    val canReceive = mutableStateOf(true)
    val finishAllReward = mutableStateOf(false)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .graphicsLayer { clip = false }, contentAlignment = Alignment.CenterStart
    ) {
        RotateBox(
            modifier = boxModifier,
            boxWidth = boxWidth,
            canReceive = canReceive,
            finishAllReward = finishAllReward
        )
    }
}
