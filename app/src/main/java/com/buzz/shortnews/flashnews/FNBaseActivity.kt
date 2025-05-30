package com.buzz.shortnews.flashnews

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashExListener
import com.buzz.shortnews.flashnews.core.common.dispatcher.Dispatcher
import com.buzz.shortnews.flashnews.core.common.dispatcher.NewsDispatchers
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
open class FNBaseActivity : ComponentActivity(), ATSplashExListener {


    @Dispatcher(NewsDispatchers.IO)
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    var showSplashHolder by mutableStateOf(true)


    override fun onAdLoaded(p0: Boolean) {

    }

    override fun onAdLoadTimeout() {
    }

    override fun onNoAdError(p0: AdError?) {
    }

    override fun onAdShow(p0: ATAdInfo?) {
//        businessSDKUtil.loadSplashAd()

    }

    override fun onAdClick(p0: ATAdInfo?) {
    }

    override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
//        splashContainer.removeAllViews()
    }

    override fun onDeeplinkCallback(p0: ATAdInfo?, p1: Boolean) {
    }

    override fun onDownloadConfirm(p0: Context?, p1: ATAdInfo?, p2: ATNetworkConfirmInfo?) {
    }
}

@Composable
fun SplashContainerViewGroup(
    onContainerReady: (ViewGroup) -> Unit
) {
    AndroidView(factory = { context ->
        // 创建原生 ViewGroup（例如 FrameLayout）
        val container = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(android.graphics.Color.BLUE)
        }

        // 创建 ComposeView，并添加进 ViewGroup
        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        }

        container.addView(composeView)

        // ✅ 把这个 ViewGroup 回传出去
        onContainerReady(container)

        container
    })
}
