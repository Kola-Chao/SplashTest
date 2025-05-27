package com.buzz.shortnews.flashnews.core.business

import android.app.Activity
import android.content.Context
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATSDK
import com.anythink.core.api.ATShowConfig
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeNetworkListener
import com.anythink.nativead.api.NativeAd
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashExListener
import com.buzz.shortnews.flashnews.core.ui.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


class BusinessSDK @Inject constructor(
    private val context: Context,
    private val appScope: CoroutineScope,
) : ATNativeNetworkListener {
    companion object {
        const val TAG = "BusinessSDK"
    }

    private var splashAd: ATSplashAd? = null
    var nativeAd: ATNative? = null

    private val isNativeAdLoading = AtomicBoolean(false)

    // 在 ViewModel 中定义
    private val _nativeAdCacheFlow = MutableSharedFlow<NativeAd?>()

    // 转换为 StateFlow
    val nativeAdCacheFlow: StateFlow<NativeAd?> = _nativeAdCacheFlow
        .distinctUntilChanged()
        .stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null // 初始值
        )

    init {
        appScope.launch {
            if (BuildConfig.DEBUG) {
                ATSDK.setNetworkLogDebug(true)
                ATSDK.integrationChecking(context)
            }
            try {
                ATSDK.init(context, BuildConfig.TOPON_APPID, BuildConfig.TOPON_APPKEY)
//                ATDebuggerUITest.showDebuggerUI(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun initSplashAD(listener: ATSplashExListener) {
        if (BuildConfig.DEVELOPER_MODE) return
        if (splashAd != null) return
        splashAd = ATSplashAd(context, BuildConfig.TOPON_AD_SPLASH, listener)
        splashAd?.loadAd()
        splashAd?.setAdRevenueListener { adInfo ->
            Timber.d("setAdRevenueListener:${adInfo.ecpm}")
        }
        Timber.d("initSplashAD")
    }

    fun getSplashAd(): ATSplashAd? {
        return splashAd
    }

//    fun loadSplashAd() {
//        splashAd?.loadAd()
//    }

    fun initAllAD(activity: Activity) {
        Timber.d("initAllAD")
        if (BuildConfig.DEVELOPER_MODE) return
        if (nativeAd == null) {
            nativeAd = ATNative(
                context, BuildConfig.TOPON_AD_NATIVE, this@BusinessSDK
            )
            loadNativeAd()
        }
    }


    private fun handleAdjustRevenueReport(atAdInfo: ATAdInfo?) {
        //adjust4.38.1及以上支持
        val adjustAdRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_TOPON)
        adjustAdRevenue.setRevenue(
            atAdInfo?.publisherRevenue,
            atAdInfo?.currency
        )

        //可选配置
        adjustAdRevenue.adRevenueNetwork = atAdInfo?.networkName
        adjustAdRevenue.adRevenueUnit = atAdInfo?.adsourceId
        adjustAdRevenue.adRevenuePlacement = atAdInfo?.placementId

        //发送收益数据
        Adjust.trackAdRevenue(adjustAdRevenue)
        Timber.d("onAdRevenuePaid: report adjust success")
    }


    fun refreshNativeAd() {
        loadNativeAd()
    }


    override fun onNativeAdLoaded() {
        isNativeAdLoading.set(false) // 原子修改
        Timber.d("onNativeAdLoaded:nativeAd:${nativeAd}")
        nativeAd?.getNativeAd(
            ATShowConfig.Builder()
                .scenarioId("news")
                .showCustomExt(null)
                .build()
        )?.let {
            appScope.launch {
                Timber.d("onNativeAdLoaded: nativeAdCacheFLow.emit")
                _nativeAdCacheFlow.emit(it)
            }
        }
    }

    override fun onNativeAdLoadFail(p0: AdError?) {
        Timber.d("onNativeAdLoadFail:${p0}")
        appScope.launch {
            delay(10_000)
            isNativeAdLoading.set(false) // 原子修改
            loadNativeAd()
        }
    }

    private fun loadNativeAd() {
        if (!isNativeAdLoading.compareAndSet(false, true)) return //检查并设置
        nativeAd?.makeAdRequest()
    }
}