package com.buzz.shortnews.flashnews.core.designsystem.component.webview


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.MutableContextWrapper
import android.graphics.Color
import android.net.Uri
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import com.buzz.shortnews.flashnews.core.ui.BuildConfig
import java.lang.ref.WeakReference

class WebViewManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: WebViewManager? = null
        private const val APP_CACHE_DIRNAME = "/activity"

        private fun getInstance() = INSTANCE ?: synchronized(WebViewManager::class.java) {
            INSTANCE ?: WebViewManager().also { INSTANCE = it }
        }

        fun prepare(context: Context) {
            getInstance().prepare(context)
        }

        fun destroy() {
            getInstance().destroy()
        }

        fun obtain(context: Context, url: String): WebView {
            return getInstance().obtain(context, url)
        }

        fun recycle(webView: WebView) {
            getInstance().recycle(webView)
        }

    }

    private val webViewMap = mutableMapOf<String, WebView>()
    private val webViewQueue: ArrayDeque<WebView> = ArrayDeque()
    private var lastBackWebView: WeakReference<WebView?> = WeakReference(null)

    private fun getWebView(context: Context): WebView {
        val webView = if (webViewQueue.isEmpty()) {
            create(MutableContextWrapper(context))
        } else {
            webViewQueue.removeFirst()
        }
        prepare(MutableContextWrapper(context.applicationContext))
        return webView
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun create(context: Context): WebView {
        val webView = WebView(context)
        webView.apply {
            setBackgroundColor(Color.TRANSPARENT)
            isVerticalScrollBarEnabled = false
        }
        val settings: WebSettings = webView.settings
//        val cacheDirPath: String = context.filesDir.absolutePath + APP_CACHE_DIRNAME
        settings.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            loadWithOverviewMode = true
            databaseEnabled = true
            domStorageEnabled = true
//            databasePath = cacheDirPath
            textZoom = 100 //解决webview字体跟随系统字体大小 变化
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            mediaPlaybackRequiresUserGesture = false
        }


        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        WebView.setWebContentsDebuggingEnabled(
            BuildConfig.DEBUG
        )
        return webView
    }

    private fun prepare(context: Context) {
        if (webViewQueue.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                webViewQueue.add(create(MutableContextWrapper(context.applicationContext)))
                false
            }
        }
    }

    private fun destroy() {
        try {
            lastBackWebView.clear()
            webViewMap.destroyWebView()
            webViewQueue.destroyWebView()
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message.toString())
        }
    }

    private fun obtain(context: Context, url: String): WebView {
        val webView = webViewMap.getOrPut(url) {
            getWebView(MutableContextWrapper(context))
        }
        if (webView.parent != null) {
            (webView.parent as ViewGroup).removeView(webView)
        }
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        return webView
    }

    private fun recycle(webView: WebView) {
        try {
            webView.removeParentView()
            if (lastBackWebView.get() == webView) {
                destroy()
                //重新缓存一个webView
                prepare(webView.context)
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message.toString())
        }
    }

    private fun WebView.removeParentView(): WebView {
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }
        val contextWrapper = context as MutableContextWrapper
        contextWrapper.baseContext = context.applicationContext
        return this
    }

    private fun MutableList<WebView>.destroyWebView() {
        forEach {
            it.removeParentView()
            it.removeAllViews()
            it.destroy()
        }
        clear()
    }

    private fun MutableMap<String, WebView>.destroyWebView() {
        values.toList().forEach {
            it.removeParentView()
            it.removeAllViews()
            it.destroy()
        }
        clear()
    }

}