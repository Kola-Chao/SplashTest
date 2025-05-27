package com.buzz.shortnews.flashnews

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    // 内置的广告容器
    lateinit var splashContainer: FrameLayout

    var showSplashHolder by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Update the uiState
        lifecycleScope.launch {
            // 初始化广告容器
            splashContainer = FrameLayout(this@FNBaseActivity).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId() // 给容器一个唯一 ID（如需 fragment 操作可用）
            }

            // 将容器作为内容视图
            setContentView(splashContainer)
            splashContainer.visibility = View.INVISIBLE
        }
    }

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
        splashContainer.visibility = ViewGroup.INVISIBLE
    }

    override fun onDeeplinkCallback(p0: ATAdInfo?, p1: Boolean) {
    }

    override fun onDownloadConfirm(p0: Context?, p1: ATAdInfo?, p2: ATNetworkConfirmInfo?) {
    }
}
