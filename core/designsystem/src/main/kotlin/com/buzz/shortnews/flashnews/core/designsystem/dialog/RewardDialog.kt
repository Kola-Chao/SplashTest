package com.buzz.shortnews.flashnews.core.designsystem.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.buzz.shortnews.flashnews.core.designsystem.component.StrokedGradientText
import com.buzz.shortnews.flashnews.core.designsystem.singleClick
import com.buzz.shortnews.flashnews.core.designsystem.theme.CustomGameStyleTypography
import com.buzz.shortnews.flashnews.core.designsystem.theme.NewsTheme
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FFB64E03
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import com.buzz.shortnews.flashnews.core.ui.commonString

@Composable
fun RewardDialog(
    balance: String,
    coin: Int,
    dismissCallback: () -> Unit
) {
    BaseDialog(
        onDismissRequest = dismissCallback,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            decorFitsSystemWindows = true,
            usePlatformDefaultWidth = false
        )
    ) {
        RewardDialogContent(balance, coin, dismissCallback)
    }
}

@Composable
private fun RewardDialogContent(balance: String, coin: Int, dismissCallback: () -> Unit) {
    NewsTheme(typography = CustomGameStyleTypography) {
        Box(
            modifier = Modifier
                .width(315.dp)
                .wrapContentHeight()
                .background(color = Color.Transparent)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 82.dp)
                    .size(268.dp, 242.dp)
                    .align(Alignment.Center),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = commonDrawable.common_diaog_content_bg),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ConstraintLayout(
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {
                        val (icon, balanceView, cashView) = createRefs()

                        Image(
                            modifier = Modifier
                                .size(76.dp)
                                .constrainAs(icon) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                },
                            painter = painterResource(id = commonDrawable.icon_coin_more),
                            contentDescription = null,
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .constrainAs(cashView) {
                                    top.linkTo(icon.bottom)
                                    start.linkTo(icon.start)
                                    end.linkTo(icon.end)
                                },
                            text = "+$coin",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = color_FFB64E03
                        )

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX = -6.dp.toPx()
                                    translationY = 14.dp.toPx()
                                }
                                .wrapContentWidth()
                                .height(23.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color(0xFF92C30A),
                                            Color(0xFF5BC30A)
                                        )
                                    ),
                                    shape = RoundedCornerShape(999.dp)
                                )
                                .constrainAs(balanceView) {
                                    bottom.linkTo(cashView.top)
                                    start.linkTo(cashView.end)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = "≈${balance}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(212.dp, 52.dp)
                            .singleClick {
                                dismissCallback.invoke()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(212.dp, 17.dp)
                                .offset(y = 5.dp)
                                .align(Alignment.BottomCenter),
                            painter = painterResource(id = commonDrawable.common_button_shadow),
                            contentDescription = null
                        )
                        Image(
                            modifier = Modifier.size(198.dp, 52.dp),
                            painter = painterResource(id = commonDrawable.common_diaog_btn_bg),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                        Text(
                            text = stringResource(id = commonString.common_Reward),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Image(
                modifier = Modifier.size(315.dp, 121.dp),
                painter = painterResource(id = commonDrawable.common_dialog_top_bg),
                contentDescription = null
            )

            StrokedGradientText(
                text = stringResource(id = commonString.common_Congratulations),
                fontSize = 22.sp,
                strokeWidth = 1.dp,
                strokeColor = Color(0xFFC42D22),
                shadowOffsetY = 2.dp,
                shadowColor = Color(0xFFAD1A10),
                gradientColors = listOf(
                    Color(0xFFFFF100),
                    Color(0xFFFAD000)
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.TopCenter)
                    .padding(top = 46.dp)
            )

            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp)
                    .size(32.dp)
                    .singleClick { dismissCallback.invoke() },
                painter = painterResource(id = commonDrawable.common_dialog_close),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun RewardDialogContentPreview() {
    RewardDialogContent("50", 500) {}
}