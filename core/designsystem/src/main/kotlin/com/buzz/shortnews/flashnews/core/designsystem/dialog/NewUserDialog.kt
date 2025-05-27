package com.buzz.shortnews.flashnews.core.designsystem.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.buzz.shortnews.flashnews.core.analytics.AnalyticsEvent
import com.buzz.shortnews.flashnews.core.analytics.LocalAnalyticsHelper
import com.buzz.shortnews.flashnews.core.designsystem.component.StrokedGradientText
import com.buzz.shortnews.flashnews.core.designsystem.singleClick
import com.buzz.shortnews.flashnews.core.designsystem.theme.CustomGameStyleTypography
import com.buzz.shortnews.flashnews.core.designsystem.theme.NewsTheme
import com.buzz.shortnews.flashnews.core.model.Config
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import com.buzz.shortnews.flashnews.core.ui.commonString

@Composable
fun NewUserDialog(
    coin: Int,
    isShow: State<Boolean>,
    dismissCallback: () -> Unit
) {
    if (isShow.value) {
        BaseDialog(
            onDismissRequest = dismissCallback,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = true,
                decorFitsSystemWindows = true,
                usePlatformDefaultWidth = false
            )
        ) {
            NewUserContent(
                coin = coin,
                dismissCallback = dismissCallback
            )
        }
    }
}

@Composable
fun NewUserContent(
    coin: Int,
    dismissCallback: () -> Unit
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    LaunchedEffect(Unit) {
        analyticsHelper.logEvent(
            event = AnalyticsEvent(
                pageTitle = AnalyticsEvent.PageTitle.newUserStagePage,
                event = AnalyticsEvent.Event.FNNewGiftShowPage
            )
        )
    }
    NewsTheme(typography = CustomGameStyleTypography) {
        Column(
            modifier = Modifier
                .width(315.dp)
                .wrapContentHeight()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .singleClick {
                    dismissCallback.invoke()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.TopCenter
            ) {
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
            }

            Image(
                modifier = Modifier.size(178.dp),
                painter = painterResource(id = commonDrawable.new_user_box),
                contentDescription = null
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(42.dp),
                    painter = painterResource(id = commonDrawable.common_coin),
                    contentDescription = null
                )
                StrokedGradientText(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "+$coin",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    strokeWidth = 0.dp,
                    strokeColor = Color(0xFFC42D22),
                    shadowOffsetY = 0.dp,
                    shadowColor = Color(0x7D000000),
                    gradientColors = listOf(
                        Color(0xFFFFE16B),
                        Color(0xFFFFCB00),
                    ),
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .size(183.dp, 65.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = commonDrawable.new_user_btn),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                StrokedGradientText(
                    text = stringResource(id = commonString.common_OPEN),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    strokeWidth = 1.dp,
                    strokeColor = Color(0xFF237C0B),
                    shadowOffsetY = 1.dp,
                    shadowColor = Color(0xFF237C0B),
                    gradientColors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF),
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun NewUserContentPreview() {
    NewUserContent(10000) {}
}
