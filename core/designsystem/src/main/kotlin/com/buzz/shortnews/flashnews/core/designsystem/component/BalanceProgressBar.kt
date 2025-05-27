package com.buzz.shortnews.flashnews.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.singleClick
import com.buzz.shortnews.flashnews.core.designsystem.theme.CustomGameStyleTypography
import com.buzz.shortnews.flashnews.core.designsystem.theme.NewsTheme
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF0D6800
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import com.buzz.shortnews.flashnews.core.ui.commonString

@Composable
fun BalanceProgressBar(
    modifier: Modifier,
    coinState: State<Int>,
    coinLevelState: State<CoinLevel>,
    progressColor: List<Color> = listOf(Color(0xFF9DE703), Color(0xFF61E602), Color(0xFF189A01)),
    shadowColor: Color? = null,
    showRedeem: Boolean = true,
    showRedeemIcon: Boolean = false,
    coinOffset: (Offset) -> Unit,
    redeemClick: () -> Unit = {},
) {
    NewsTheme(typography = CustomGameStyleTypography) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                BalanceProgress(
                    progressColor = progressColor,
                    coinState = coinState,
                    coinLevelState = coinLevelState,
                    shadowColor = shadowColor,
                    coinOffset = coinOffset
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .height(34.dp + if (shadowColor != null) 2.dp else 0.dp)
                    .wrapContentWidth()
                    .then(
                        if (showRedeem) Modifier.singleClick { redeemClick.invoke() }
                        else Modifier // 不添加点击
                    )
                    .drawWithContent {
                        if (showRedeem) {
                            drawContent() // 只有为 true 时才绘制内容
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.matchParentSize(),
                    painter = painterResource(id = commonDrawable.redeem_btn_bg_green),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                if (showRedeemIcon) {
                    Image(
                        modifier = Modifier
                            .padding(start = 18.dp, end = 18.dp, bottom = 3.dp)
                            .size(22.dp, 18.dp),
                        painter = painterResource(id = commonDrawable.ic_redeem),
                        contentDescription = null
                    )
                } else {
                    StrokedGradientText(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .offset(y = (-1).dp),
                        text = stringResource(id = commonString.common_Redeem),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = 1.dp,
                        strokeColor = Color(0xFF097117),
                        shadowOffsetY = 0.5.dp,
                        shadowColor = Color(0xFF097117),
                        gradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)),
                    )
                }
            }

        }
    }
}

@Composable
fun BalanceProgress(
    progressColor: List<Color> = listOf(Color(0xFF9DE703), Color(0xFF61E602), Color(0xFF189A01)),
    coinState: State<Int>,
    coinLevelState: State<CoinLevel>,
    shadowColor: Color? = null,
    coinOffset: (Offset) -> Unit,
) {
    val progress =
        remember { derivedStateOf { coinState.value / coinLevelState.value.levelCoin.toFloat() } }
    val coinInfo =
        remember { derivedStateOf { "${coinState.value}/${coinLevelState.value.levelCoin}(${coinLevelState.value.levelInfo})" } }
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                //投影
                .then(
                    if (shadowColor != null) {
                        Modifier
                            .background(color = shadowColor, RoundedCornerShape(99.dp))
                            .padding(bottom = 2.dp)
                    } else {
                        Modifier
                    }
                )
                //描边
                .background(Color(0xFFFFD9BE), RoundedCornerShape(99.dp))
                .padding(2.dp)
                //进度条背景色
                .background(Color(0xFFD68042), RoundedCornerShape(99.dp))
                .padding(2.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .clip(RoundedCornerShape(99.dp))
            ) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = progressColor
                    ),
                    size = size.copy(width = size.width * progress.value)
                )
            }

            StrokedGradientText(
                text = coinInfo.value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                strokeWidth = 1.dp,
                strokeColor = Color(0xFF92572d),
                shadowOffsetY = 2.dp,
                shadowColor = Color(0x00000000),
                isAutoSize = true,
                gradientColors = listOf(Color.White, Color.White),
                modifier = Modifier
                    .padding(start = 33.dp)
                    .offset(y = 1.dp)
                    .fillMaxWidth()
            )
        }

        Image(
            modifier = Modifier
                .padding(start = 1.dp)
                .size(30.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    val windowOffset = layoutCoordinates.positionInWindow()
                    coinOffset.invoke(windowOffset)
                },
            painter = painterResource(id = commonDrawable.common_coin),
            contentDescription = null
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun BalanceProgressTitleBarPreview() {
    val coinState = remember {
        mutableIntStateOf(500)
    }
    val coinInfo = remember {
        mutableStateOf(CoinLevel(5000, "$5"))
    }
    BalanceProgressBar(
        modifier = Modifier,
        coinState = coinState, coinLevelState = coinInfo,
        progressColor = listOf(Color(0xFF9DE703), Color(0xFF61E602), Color(0xFF189A01)),
        shadowColor = Color(0xFFD09B75),
        coinOffset = {}
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun BalanceProgressPreview() {
    val coinState = remember {
        mutableIntStateOf(20000)
    }
    val coinInfo = remember {
        mutableStateOf(CoinLevel(2000000, "$200"))
    }

    Column(
        modifier = Modifier
            .height(32.dp)
            .width(225.dp)
    ) {
        BalanceProgress(coinState = coinState, coinLevelState = coinInfo, coinOffset = {})
    }
}