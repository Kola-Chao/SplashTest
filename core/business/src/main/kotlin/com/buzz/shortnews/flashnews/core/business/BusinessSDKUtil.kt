package com.buzz.shortnews.flashnews.core.business

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.tooling.preview.Devices
import com.anythink.core.api.ATShowConfig
import com.buzz.shortnews.flashnews.core.ui.commonString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BusinessSDKUtil @Inject constructor(
    private val businessSDK: BusinessSDK,
    private val appScope: CoroutineScope,
) {
    // 内置的广告容器
    var splashContainer: ViewGroup? = null

    /**
     * 显示 Splash 广告
     * @param container 可选：广告展示容器，如果为空则使用默认 splashContainer
     */
    fun showSplashAd(
        activity: Activity,
//        listener: ATSplashAdListener
    ) {
        splashContainer?.let {
            appScope.launch {
                val splashAd = businessSDK.getSplashAd() ?: return@launch
                Timber.d("showSplashAd:${splashAd.isAdReady}")
                if (splashAd.isAdReady) {
                    it.visibility = ViewGroup.VISIBLE
                    val showConfig = ATShowConfig.Builder()
                        .scenarioId("splash")
                        .build()
//            splashAd.setAdListener(listener)
                    splashAd.show(activity, it, null, showConfig)
                } else {
                    splashAd.loadAd()
                }
            }
        }
    }


    suspend fun splashAdReady(): Boolean {
        val splashAd = businessSDK.getSplashAd() ?: return false
        Timber.d("splashAdReady:${splashAd.isAdReady}")
        return splashAd.isAdReady
    }


    fun loadSplashAd() {
        val splashAd = businessSDK.getSplashAd() ?: return
        Timber.d("loadSplashAd:${splashAd.isAdReady}")
        if (!splashAd.isAdReady) {
            splashAd.loadAd()
        }
    }
}