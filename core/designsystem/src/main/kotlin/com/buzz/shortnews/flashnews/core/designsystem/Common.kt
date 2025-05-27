package com.buzz.shortnews.flashnews.core.designsystem

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.graphics.MaskFilter
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.buzz.shortnews.flashnews.core.ui.BuildConfig
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt
import kotlin.random.Random


import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect

import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density

fun randomColorBg(): Color {
    // 生成随机颜色
    val randomColor = Color(
        Random.nextFloat(),
        Random.nextFloat(),
        Random.nextFloat()
    )
    return randomColor
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.appBackground(
    color: Color,
): Modifier {
    return if (BuildConfig.DEVELOPER_MODE) {
        Modifier.background(color = randomColorBg())
    } else {
        Modifier.background(color = color)
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    val shimmerOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.Gray.copy(alpha = 0.3f),
            Color.Gray.copy(alpha = 0.1f),
            Color.Gray.copy(alpha = 0.3f)
        ),
        start = Offset(x = shimmerOffset, y = 0f),
        end = Offset(x = shimmerOffset + 500f, y = 0f)
    )

    background(shimmerBrush)
}


@Composable
fun Modifier.singleClick(
    debounceTime: Long = 1000L,
    isRipples: Boolean = false,
    onClick: () -> Unit
): Modifier = composed {
    if (isRipples) {
        val localIndication = LocalIndication.current
        val interactionSource = if (localIndication is IndicationNodeFactory) {
            null
        } else {
            remember { MutableInteractionSource() }
        }
        singleClick(
            debounceTime,
            true,
            interactionSource,
            localIndication,
            onClick
        )
    } else {
        singleClick(
            debounceTime,
            true,
            null,
            null,
            onClick
        )
    }

}

@Composable
fun Modifier.singleClick(
    debounceTime: Long = 1000L,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier = composed {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    this.clickable(
        enabled = enabled && isClickable,
        interactionSource = interactionSource,
        indication = indication
    ) {
        if (isClickable) {
            isClickable = false
            coroutineScope.launch {
                onClick()
                delay(debounceTime)
                isClickable = true
            }
        }
    }
}

@Composable
fun GlideImageInCompose(
    modifier: Modifier,
    model: String,
    contentScale: ImageView.ScaleType? = ImageView.ScaleType.CENTER_CROP
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                scaleType = contentScale
            }
        },
        update = { imageView ->
            if (BuildConfig.DEVELOPER_MODE) {
                Glide.with(imageView.context)
                    .load(model)
                    .placeholder(commonDrawable.placeholder_normal)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.e("onLoadFailed:url:$model \n msg:${e?.message}")
                            e?.logRootCauses("Common")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.e("loadSuccess")
                            return false
                        }

                    })
                    .into(imageView)
            } else {
                Glide.with(imageView.context)
                    .load(model)
                    .placeholder(commonDrawable.placeholder_normal)
                    .into(imageView)
            }
        }
    )
}

//跑马灯效果
fun Modifier.marquee() = composed {
    var offset by remember { mutableFloatStateOf(0f) }
    val textWidth = remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        while (true) {
            // 根据文本宽度计算滚动距离
            val scrollAmount = with(density) { textWidth.floatValue.dp.toPx() }
            // 如果文本宽度大于容器宽度，则开始滚动
            if (scrollAmount > 0) {
                val duration = (scrollAmount * 30).toLong() // 调整30这个值可以改变滚动速度
                animate(
                    0f,
                    scrollAmount,
                    animationSpec = tween(durationMillis = duration.toInt())
                ) { value, _ ->
                    offset = -value
                }
                delay(500) // 滚动到末尾后暂停500ms
                animate(
                    scrollAmount,
                    0f,
                    animationSpec = tween(durationMillis = duration.toInt())
                ) { value, _ ->
                    offset = -value
                }
                delay(500) // 滚动回起始位置后暂停500ms
            } else {
                delay(100) // 不需要滚动时也保持协程运行
            }
        }
    }


    drawWithContent {
        drawContent()
        textWidth.floatValue = size.width
    }
        .offset { IntOffset(offset.roundToInt(), 0) }
        .clipToBounds()
}

/**
 * 绘制纯圆，在外部通过modifier来把它压扁
 */
@Composable
fun OvalBar(modifier: Modifier = Modifier, color: Color) {
    Canvas(
        modifier = modifier
    ) {
        drawOval(
            color = color,
            size = size
        )
    }
}


enum class ShadowType { Inner, Outer }

fun Modifier.advancedShadow(
    color: Color,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    spreadRadius: Dp = 0.dp,
    shadowType: ShadowType = ShadowType.Outer,
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier = composed {
    val density = LocalDensity.current
    val offsetXpx = with(density) { offsetX.toPx() }
    val offsetYpx = with(density) { offsetY.toPx() }
    val blurRadiusPx = with(density) { blurRadius.toPx() }
    val spreadRadiusPx = with(density) { spreadRadius.toPx() }

    this.then(
        when (shadowType) {
            ShadowType.Outer -> outerShadowModifier(
                color = color,
                offset = Offset(offsetXpx, offsetYpx),
                blurRadius = blurRadiusPx,
                spreadRadius = spreadRadiusPx,
                shape = shape,
                density = density
            )

            ShadowType.Inner -> innerShadowModifier(
                color = color,
                offset = Offset(offsetXpx, offsetYpx),
                blurRadius = blurRadiusPx,
                shape = shape,
                density = density
            )
        }
    )
}

// 外投影实现
private fun outerShadowModifier(
    color: Color,
    offset: Offset,
    blurRadius: Float,
    spreadRadius: Float,
    shape: Shape,
    density: Density
): Modifier = Modifier.drawBehind {
    val outline = shape.createOutline(size, LayoutDirection.Ltr, density)
    val path = Path().apply { addOutline(outline) }

    // 扩展阴影区域
    val spreadPath = Path().apply {
        addPath(path)
        translate(Offset(-spreadRadius, -spreadRadius))
        addPath(path)
        translate(Offset(spreadRadius * 2, spreadRadius * 2))
    }

    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            this.color = color
            asFrameworkPaint().apply {
                maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            }
        }

        canvas.translate(offset.x, offset.y)
        canvas.drawPath(spreadPath, paint)
    }
}

// 内投影实现
private fun innerShadowModifier(
    color: Color,
    offset: Offset,
    blurRadius: Float,
    shape: Shape,
    density: Density
): Modifier = Modifier.drawBehind {
    val outline = shape.createOutline(size, LayoutDirection.Ltr, density)
    val path = Path().apply { addOutline(outline) }

    drawIntoCanvas { canvas ->
        // 绘制背景
        drawPath(path, color = Color.White)

        // 绘制内阴影
        canvas.saveLayer(Rect(0f, 0f, size.width, size.height), Paint())
        canvas.clipPath(path)

        val shadowPaint = Paint().apply {
            this.color = color
            asFrameworkPaint().apply {
                maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            }
        }

        canvas.translate(offset.x, offset.y)
        canvas.drawRect(
            Rect(-1000f, -1000f, size.width + 1000f, size.height + 1000f),
            shadowPaint
        )
        canvas.restore()
    }
}

/**
 * 设置灰度0的内容
 */
@Composable
fun GrayscaleBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val colorMatrix = ColorMatrix().apply {
        setToSaturation(0f)
    }

    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    Box(
        modifier = modifier.drawWithContent {
            drawIntoCanvas { canvas ->
                val rect = Rect(0f, 0f, size.width, size.height) // 明确区域
                canvas.saveLayer(rect, paint)
                drawContent()
                canvas.restore()
            }
        }
    ) {
        content()
    }
}

