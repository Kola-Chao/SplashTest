package com.buzz.shortnews.flashnews.core.common.dispatcher

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val newsDispatcher: NewsDispatchers)

enum class NewsDispatchers {
    Default,
    IO,
    Main,
}