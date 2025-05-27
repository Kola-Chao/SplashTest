package com.buzz.shortnews.flashnews.core.designsystem.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.buzz.shortnews.flashnews.core.ui.commonDrawable

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        // 设置默认选项
        builder.setDefaultRequestOptions(
            RequestOptions()
                .placeholder(commonDrawable.placeholder_normal) // 默认占位图
//                .error()            // 默认错误图
                .centerCrop()
        )
    }
}