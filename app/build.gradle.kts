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
    alias(libs.plugins.newsapp.android.application)
    alias(libs.plugins.newsapp.android.application.compose)
    alias(libs.plugins.newsapp.android.application.flavors)
//    alias(libs.plugins.newsapp.android.application.jacoco)
    alias(libs.plugins.newsapp.android.application.firebase)
    alias(libs.plugins.newsapp.hilt)
//    id("com.google.android.gms.oss-licenses-plugin")
//    alias(libs.plugins.baselineprofile)
//    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.buzz.shortnews.flashnews"

    defaultConfig {
        applicationId = "com.example.app"
        versionCode =
            if (project.hasProperty("versionCode"))
                project.property("versionCode").toString().toInt()
            else
                120
        versionName =
            if (project.hasProperty("versionName"))
                project.property("versionName").toString()
            else
                "1.2.0"

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner =
            "com.buzz.shortnews.flashnews.core.testing.NiaTestRunner"
    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Ensure Baseline Profile is fresh for release builds.
//            baselineProfile.automaticGenerationDuringBuild = true
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

dependencies {

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.kotlinx.coroutines.guava)
    //    implementation(libs.coil.kt)
//    implementation(libs.glide.compose)
    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.blankj)
    implementation(libs.google.gms.ads.identifier)
    implementation(libs.google.gms.base)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.accompanist.permissions)


    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)

    kspTest(libs.hilt.compiler)


    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlin.test)

    testDemoImplementation(libs.androidx.navigation.testing)
    testDemoImplementation(libs.robolectric)
    testDemoImplementation(libs.roborazzi)


    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlin.test)

}

//baselineProfile {
//    // Don't build on every iteration of a full assemble.
//    // Instead enable generation directly for the release build variant.
//    automaticGenerationDuringBuild = false
//
//    // Make use of Dex Layout Optimizations via Startup Profiles
//    dexLayoutOptimization = true
//}
//
//dependencyGuard {
//    configuration("prodReleaseRuntimeClasspath")
//}
