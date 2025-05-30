package com.buzz.shortnews.flashnews

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.tracing.trace
import com.anythink.core.api.ATAdInfo
import com.anythink.splashad.api.ATSplashAdExtraInfo

import com.buzz.shortnews.flashnews.core.business.BusinessSDK
import com.buzz.shortnews.flashnews.core.business.BusinessSDKUtil
import com.buzz.shortnews.flashnews.core.designsystem.NewsApp
import com.buzz.shortnews.flashnews.core.designsystem.SplashScreenDialog

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class MainiActivity : FNBaseActivity() {

    @Inject
    lateinit var businessSDK: BusinessSDK

    @Inject
    lateinit var businessSDKUtil: BusinessSDKUtil


    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        businessSDK.initSplashAD(this)

        setContent {
            var finishSplashScreen by remember { mutableStateOf(false) }
            var shouldShowApp by remember { mutableStateOf(false) } // ✅ 控制只初始化一次

            val duration = remember { mutableIntStateOf(10 * 1000) }


            // ✅ 副作用：只执行一次
            LaunchedEffect(finishSplashScreen) {
                if (finishSplashScreen && !shouldShowApp) {
                    shouldShowApp = true
                    businessSDK.initAllAD(this@MainiActivity)
                }
            }

            CompositionLocalProvider {  // ✅ 只在 splash 结束后显示 NewsApp，且只显示一次
                if (shouldShowApp) {
                    SplashContainerViewGroup {
                        businessSDKUtil.splashContainer = it
                    }
                }

                if (showSplashHolder) {
                    SplashScreenDialog(duration, onFinish = {
                        finishSplashScreen = true
                        finishSplashLoading()
                    }, dismissCallback = {})
                }

            }
        }
//        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.extras == null) return
    }


//    override fun getResources(): Resources {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
//        return super.getResources()
//    }

    override fun onAdShow(p0: ATAdInfo?) {
        super.onAdShow(p0)
        finishSplashLoading()
    }

    override fun onAdLoaded(p0: Boolean) {
        super.onAdLoaded(p0)
        //当前Activity是前台的时候，才会显示开屏广告-负责冷启动和热启动时没准备好广告在Loading中加载完成广告的逻辑
        if (showSplashHolder) {
            businessSDKUtil.showSplashAd(
                this@MainiActivity,
//                    this@MainiActivity,
            )
        }
    }

    override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
        super.onAdDismiss(p0, p1)
        finishSplashLoading()
        businessSDKUtil.loadSplashAd()
    }

    private fun finishSplashLoading() {
        showSplashHolder = false
    }
}
