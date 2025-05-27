/*
 * Copyright 2021 The Android Open Source Project
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

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        //Anythink(Core)
        maven { url = uri("https://jfrog.anythinktech.com/artifactory/overseas_sdk") }
        maven { url = uri("https://jfrog.anythinktech.com/artifactory/debugger") }
        //Pubnative
        maven { url = uri("https://verve.jfrog.io/artifactory/verve-gradle-release") }
        //Pangle
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
        //Mintegral
        maven {
            url =
                uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
        //Chartboost
        maven { url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads") }
        maven { url = uri("https://cboost.jfrog.io/artifactory/chartboost-mediation") }
    }
}
rootProject.name = "SplashTest"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:business")
include(":core:ui")
include(":core:designsystem")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    ThisApp requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
