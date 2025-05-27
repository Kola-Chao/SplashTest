package com.buzz.shortnews.flashnews.core.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buzz.shortnews.flashnews.core.designsystem.dialog.BaseDialog
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import com.buzz.shortnews.flashnews.core.designsystem.theme.background_light
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF949494
import com.buzz.shortnews.flashnews.core.designsystem.theme.icon_first
import com.buzz.shortnews.flashnews.core.designsystem.theme.text_title
import com.buzz.shortnews.flashnews.core.ui.commonString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Composable
fun SplashScreenDialog(
    duration: State<Int>,
    onFinish: () -> Unit,
    dismissCallback: () -> Unit
) {
    BaseDialog(
        onDismissRequest = dismissCallback, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            decorFitsSystemWindows = true,
            usePlatformDefaultWidth = false
        )
    ) {
        SplashScreen(duration, onFinish)
    }
}

@Composable
fun SplashScreen(duration: State<Int>, onFinish: () -> Unit) {
//    val analyticsHelper = LocalAnalyticsHelper.current
//    analyticsHelper.logEvent(
//        AnalyticsEvent(
//            AnalyticsEvent.PageTitle.test_page,
//            AnalyticsEvent.Event.test_event
//        )
//    )

    //绘制启动屏
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        // APP Logo
        Image(
            painter = painterResource(id = commonDrawable.splash_icon),
            contentDescription = null,
            modifier = Modifier
                .height(288.dp)
                .width(288.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // 关键：对齐到底部
                .padding(bottom = 52.dp)// 可调整底部间距
        ) {
            ProgressGroup(duration, onFinish)
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = commonString.splash_info),
                fontSize = 12.sp,
                color = color_FF949494
            )
        }
    }
}

@Composable
private fun ProgressGroup(
    duration: State<Int>,
    onFinish: () -> Unit
) {
    val width = 208.dp
    var progress by remember { mutableFloatStateOf(0f) }
    val aDuration = duration.value
    LaunchedEffect(key1 = Unit) {
        Timber.d("startLoading...$aDuration")
        //模拟加载进度
        while (progress < 0.99f) {
            delay((aDuration / 100).toLong()) // 模拟网络请求等
            progress += 0.01f
        }
        Timber.d("finishLoading...")
        onFinish() // 进度满后，切换到主界面
    }
    Text(
        text = "${(progress * 100).toInt()}%",
        textDecoration = null,
        fontSize = 12.sp,
        color = text_title
    )
    Spacer(modifier = Modifier.height(8.dp))
    // 进度条
    Box(
        modifier = Modifier
            .height(3.dp)
            .width(width)
            .clip(RoundedCornerShape(99.dp)) // 设置左右圆角
            .background(color = background_light),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(width * progress)
                .clip(RoundedCornerShape(topStart = 99.dp, bottomStart = 99.dp))
                .background(color = icon_first)
        )
    }
}
