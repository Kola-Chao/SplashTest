package com.buzz.shortnews.flashnews.core.designsystem.component.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.webkit.JavascriptInterface

class WebBridge(val context: Context, private val closeCallback: () -> Unit) {

    @JavascriptInterface
    fun close() {
        closeCallback.invoke()
    }

    @JavascriptInterface
    fun openBrowser(url: String) {
        try {
            var intent: Intent? = null
            intent = if (url.startsWith("intent")) {
                Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            } else {
                Intent("android.intent.action.VIEW", Uri.parse(url))
            }
            if (intent != null) {
                if (isHw()) {
                    intent.setPackage(getDefaultBrowser())
                }
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.component = null
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isHw(): Boolean {
        return "huawei".equals(Build.MANUFACTURER, ignoreCase = true)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun getDefaultBrowser(): String? {
        var packageName: String? = null
        var systemApp: String? = null
        var userApp: String? = null
        val userAppList: MutableList<String> = ArrayList()
//        val context: Context = Apps.application.applicationContext
        val browserIntent = Intent("android.intent.action.VIEW", Uri.parse("https://"))
        val resolveInfo =
            context.packageManager.resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolveInfo?.activityInfo != null) {
            packageName = resolveInfo.activityInfo.packageName
        }
        if (packageName == null || packageName == "android") {
            val lists = context.packageManager.queryIntentActivities(browserIntent, 0)
            for (app in lists) {
                if (app.activityInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                    systemApp = app.activityInfo.packageName
                } else {
                    userApp = app.activityInfo.packageName
                    userAppList.add(userApp)
                }
            }
            if (userAppList.contains("com.android.chrome")) {
                packageName = "com.android.chrome"
            } else {
                if (systemApp != null) {
                    packageName = systemApp
                }
                if (userApp != null) {
                    packageName = userApp
                }
            }
        }
        return packageName
    }
}