package com.buzz.shortnews.flashnews

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.adjust.sdk.Adjust

import com.buzz.shortnews.flashnews.core.business.ApplicationScope
import com.buzz.shortnews.flashnews.core.business.BusinessSDKUtil

import com.buzz.shortnews.flashnews.core.common.dispatcher.Dispatcher
import com.buzz.shortnews.flashnews.core.common.dispatcher.NewsDispatchers

import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextLong

@HiltAndroidApp
class NewsApplication : Application() {
    // 全局记录当前 activity（注意避免内存泄漏）
    var currentActivity: Activity? = null
        private set

    //    @Inject
//    lateinit var imageLoader: dagger.Lazy<ImageLoader>
    @ApplicationScope
    @Inject
    lateinit var appScope: CoroutineScope

    @Dispatcher(NewsDispatchers.IO)
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher


    @Inject
    lateinit var businessSDKUtil: BusinessSDKUtil


    override fun onCreate() {
        super.onCreate()


        // 监听应用的生命周期
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                // 可以选择清除 currentActivity，如果你只关心前台 Activity 可在此置 null
                currentActivity = null
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity === activity) {
                    currentActivity = null
                }
            }
        })
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())


    }


    private inner class AppLifecycleObserver : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            val currentActivity = (this@NewsApplication).currentActivity

            when (event) {
                Lifecycle.Event.ON_START -> {

                    if (currentActivity is MainiActivity) {
                        appScope.launch {
//                            if (businessSDKUtil.needShowSplash()) {//只有需要展示开屏的时候才显示加载页面
                            currentActivity.showSplashHolder = true
//                            }
                            if (businessSDKUtil.splashAdReady()) {//模拟一个加载进度
                                val delayTime = 5_000
                                delay(
                                    Random.nextLong(
                                        (delayTime / 5).toLong(),
                                        (delayTime / 2).toLong()
                                    )
                                )
                                businessSDKUtil.showSplashAd(
                                    currentActivity,
                                    currentActivity.splashContainer,
//                                    currentActivity
                                )
                            } else {
                                businessSDKUtil.loadSplashAd()
                            }

                        }
                    }
                }

                Lifecycle.Event.ON_STOP -> {
                    businessSDKUtil.loadSplashAd()
                }

                Lifecycle.Event.ON_RESUME -> {
                    Adjust.onResume()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Adjust.onPause()
                }

                else -> {}
            }
        }
    }
}