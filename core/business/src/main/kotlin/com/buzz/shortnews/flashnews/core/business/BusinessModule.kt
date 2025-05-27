package com.buzz.shortnews.flashnews.core.business

import android.content.Context
import com.buzz.shortnews.flashnews.core.common.dispatcher.Dispatcher
import com.buzz.shortnews.flashnews.core.common.dispatcher.NewsDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BusinessModule {
    @Provides
    @Singleton
    fun providesBusinessSDK(
        @ApplicationContext context: Context,
        @ApplicationScope appScope: CoroutineScope,
    ): BusinessSDK = BusinessSDK(context, appScope)


    @Provides
    @Singleton
    fun providesBusinessSDKUtil(
        businessSDK: BusinessSDK,
        @ApplicationScope appScope: CoroutineScope,
        @Dispatcher(NewsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @Dispatcher(NewsDispatchers.Main) mainDispatcher: CoroutineDispatcher
    ): BusinessSDKUtil = BusinessSDKUtil(
        businessSDK = businessSDK,
        appScope = appScope,
    )
}