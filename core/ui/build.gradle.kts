/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.newsapp.android.library)
    alias(libs.plugins.newsapp.android.library.compose)
    alias(libs.plugins.newsapp.android.library.jacoco)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        // 定义 BuildConfig 变量
        buildConfigField(
            "boolean",
            "DEVELOPER_MODE",
            findProperty("developerMode")?.toString()?.toBoolean()?.toString() ?: "true"
        )
//        buildConfigField(
//            "boolean",
//            "STRIKE_MODE",
//            findProperty("strikeMode")?.toString()?.toBoolean()?.toString() ?: "false"
//        )
    }
    namespace = "com.buzz.shortnews.flashnews.core.ui"

    lint {
        disable += "ResourceName"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    //Warinig:不应该依赖任何project
    api(libs.androidx.metrics)

    implementation(libs.androidx.browser)
//    implementation(libs.coil.kt)
//    implementation(libs.coil.kt.compose)
    api(libs.glide.compose)
}
