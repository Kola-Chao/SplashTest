package com.buzz.shortnews.flashnews.core.designsystem.component.webview

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@Composable
fun WebView(
    url: String,
    navigator: WebViewNavigator,
    modifier: Modifier = Modifier,
    onLoadUrl: (url: String) -> Unit = {},
    onNavigateUp: (complete: Boolean) -> Unit = {},
    onPageState: (state: PageState) -> Unit = {},
) {
    var complete = true
    var webView by remember { mutableStateOf<WebView?>(null) }
    BackHandler(true) {
        navigator.navigateBack()
    }
    webView?.let {
        LaunchedEffect(it, navigator) {
            navigator.lastLoadedUrl = it.url
            with(navigator) {
                handleNavigationEvents(
                    onBack = {
                        if (it.canGoBack()) {
                            it.goBack()
                        } else {
                            onNavigateUp(complete)
                        }
                    },
                    reload = {
                        it.reload()
                    }
                )
            }
        }
    }

    AndroidView(
        factory = { context ->
            WebViewManager.obtain(context, url).apply {
                this.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                addJavascriptInterface(WebBridge(context) {
                    if (this.canGoBack()) {
                        this.goBack()
                    } else {
                        onNavigateUp(complete)
                    }
                }, "GSpaceInterface")

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        Timber.d("shouldOverrideUrlLoading: $url")
                        val url = request?.url.toString()
                        if (openIntent(context, url)) {
                            return true
                        }

                        if (processIntentAndApk(url)) {
                            return true
                        }

                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        navigator.lastLoadedUrl = url
                        onPageState(PageState.PAGE_STARTED)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageState(PageState.PAGE_FINISHED)
                    }

                    private fun openIntent(context: Context, url: String): Boolean {
                        try {
                            if (url.startsWith("market:")
                                || url.startsWith("https://play.google.com/store/")
                                || url.startsWith("http://play.google.com/store/")
                            ) {
                                val intent = Intent(Intent.ACTION_VIEW)
                                if (url.startsWith("market://details?id=")) {
                                    val replace =
                                        url.replace(
                                            "market://details",
                                            "https://play.google.com/store/apps/details"
                                        )
                                    intent.setData(Uri.parse(replace))
                                } else {
                                    intent.setData(Uri.parse(url))
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                                return true
                            } else if (!url.startsWith("http://")
                                && !url.startsWith("https://")
                            ) {
                                openUrl(context, url)
                                return true
                            } else if (url.contains("lz_open_browser=1")) {
                                openBrowser(context, url)
                                return true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return false
                    }

                    private fun processIntentAndApk(url: String): Boolean {
                        try {
                            val uri = Uri.parse(url)
                            var scheme = uri.scheme ?: return false
                            scheme = scheme.lowercase()
                            when (scheme) {
                                "intent" -> {
                                    val intent =
                                        Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    if (uri.host == "play.google.com") {
                                        intent.setPackage("com.android.vending")
                                    }
                                    context.startActivity(intent)
                                    return true
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return false
                    }

                    private fun openBrowser(context: Context, url: String?) {
                        if (isAppInstalled(context, "com.android.chrome")) {
                            // 创建一个 Intent，指定 ACTION_VIEW 动作和 URL
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            // 指定要使用的浏览器的包名
                            intent.setPackage("com.android.chrome") // Chrome 浏览器的包名
                            // 启动 Chrome 浏览器来处理链接
                            context.startActivity(intent)
                        } else {
                            // 如果没有安装 Chrome 浏览器，使用系统默认浏览器打开链接
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            // 启动默认浏览器来处理链接
                            context.startActivity(intent)
                        }
                    }

                    private fun isAppInstalled(context: Context, pkgName: String): Boolean {
                        val packageManager = context.packageManager
                        return try {
                            packageManager.getApplicationInfo(pkgName, 0).enabled
                        } catch (e: PackageManager.NameNotFoundException) {
                            false
                        }
                    }

                    @SuppressLint("QueryPermissionsNeeded")
                    private fun openUrl(context: Context, url: String?) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            val activityInfo = intent.resolveActivityInfo(context.packageManager, 0)
                            if (activityInfo.exported) {
                                intent.setData(Uri.parse(url))
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            }
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
                setDownloadListener { url, _, _, _, _ ->
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                this.loadUrl(url)
                onLoadUrl(url)
            }.also { webView = it }
        },
        modifier = modifier,
        onRelease = {
            WebViewManager.recycle(it)
        }
    )

}

enum class PageState {
    PAGE_STARTED,
    PAGE_FINISHED,
}

@Stable
class WebViewNavigator(
    private val scope: CoroutineScope
) {
    private sealed interface NavigationEvent {
        data object Back : NavigationEvent
        data object Reload : NavigationEvent
    }

    private val navigationEvents: MutableSharedFlow<NavigationEvent> = MutableSharedFlow()

    var lastLoadedUrl: String? by mutableStateOf(null)
        internal set
    var progress: Float by mutableFloatStateOf(0f)
        internal set

    @OptIn(FlowPreview::class)
    internal suspend fun handleNavigationEvents(
        onBack: () -> Unit = {},
        reload: () -> Unit = {},
    ) = withContext(Dispatchers.Main) {
        navigationEvents.debounce(350).collect { event ->
            when (event) {
                NavigationEvent.Back -> onBack()
                NavigationEvent.Reload -> reload()
            }
        }
    }

    fun navigateBack() {
        scope.launch { navigationEvents.emit(NavigationEvent.Back) }
    }


    fun reload() {
        scope.launch { navigationEvents.emit(NavigationEvent.Reload) }
    }

}

@Composable
fun rememberWebViewNavigator(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): WebViewNavigator = remember(coroutineScope) { WebViewNavigator(coroutineScope) }