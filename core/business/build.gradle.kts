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
    alias(libs.plugins.newsapp.android.feature)
    alias(libs.plugins.newsapp.android.library.compose)
    alias(libs.plugins.newsapp.android.library.jacoco)
    alias(libs.plugins.newsapp.hilt)
    id("kotlinx-serialization")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")

//        buildConfigField("String", "TOPON_APPID", "h66c2fec940820")
//        buildConfigField("String", "TOPON_APPKEY", "accbc326545146ff0b02f5c0e6fbf1207")
//        buildConfigField("String", "TOPON_AD_SPLASH", "n6721ed9cbb731")
//        buildConfigField("String", "TOPON_AD_NATIVE", "n6721ed9cbb731")
//        buildConfigField("String", "TOPON_AD_REWARD", "n66c2feebca411")
//        buildConfigField("String", "TOPON_AD_INTERSTITIAL", "n66c2fedfb2fdb")
//        buildConfigField("String", "TOPON_AD_BANNER", "n6721ed894996c")
    }
    namespace = "com.buzz.shortnews.flashnews.core.business"

}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    api(libs.glide.compose)
    api(libs.adjust)
    implementation(libs.google.gms.ads.identifier)
    implementation(libs.constraintlayout.compose)
    implementation(libs.androidx.cardview)
//    api(libs.androidx.compose.foundation)
//    api(libs.androidx.compose.foundation.layout)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config)
//    //Facebook
//    prodImplementation("com.facebook.android:facebook-android-sdk:[8,9)")

    //Test
    api("com.anythink.sdk:debugger-ui:1.1.0")
    //Anythink (Necessary)
    //FIXME:注意core:base里的core-tpn和splash-tpn的版本号要和这里保持一致
    api("com.anythink.sdk:core-tpn:6.4.69")
    api("com.anythink.sdk:nativead-tpn:6.4.69")
    api("com.anythink.sdk:banner-tpn:6.4.69")
    api("com.anythink.sdk:interstitial-tpn:6.4.69")
    api("com.anythink.sdk:rewardedvideo-tpn:6.4.69")
    api("com.anythink.sdk:splash-tpn:6.4.69")

    //Androidx (Necessary)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.browser:browser:1.4.0")

    //Vungle
    implementation("com.anythink.sdk:adapter-tpn-vungle:6.4.69")
    implementation("com.vungle:vungle-ads:7.4.3")
    implementation("com.google.android.gms:play-services-basement:18.1.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")

    //UnityAds
    implementation("com.anythink.sdk:adapter-tpn-unityads:6.4.69")
    implementation("com.unity3d.ads:unity-ads:4.14.0")

    //Pubnative
    implementation("com.anythink.sdk:adapter-tpn-pubnative:6.4.69")
    implementation("net.pubnative:hybid.sdk:3.2.1")

    //Pangle
    implementation("com.anythink.sdk:adapter-tpn-pangle-nonchina:6.4.69")
    implementation("com.pangle.global:ads-sdk:6.5.0.6")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")

//    //Facebook
//    prodImplementation("com.anythink.sdk:adapter-tpn-facebook:6.4.69")
//    prodImplementation("com.facebook.android:audience-network-sdk:6.19.0")
//    prodImplementation("androidx.annotation:annotation:1.0.0")
//
//    //Admob
//    prodImplementation("com.anythink.sdk:adapter-tpn-admob:6.4.69.2")
//    prodImplementation("com.google.android.gms:play-services-ads:24.0.0")

    //Inmobi
    implementation("com.anythink.sdk:adapter-tpn-inmobi:6.4.69")
    implementation("com.inmobi.monetization:inmobi-ads-kotlin:10.8.2")

    //AppLovin
    implementation("com.anythink.sdk:adapter-tpn-applovin:6.4.69.2")
    implementation("com.applovin:applovin-sdk:13.2.0")

    //Mintegral
    implementation("com.anythink.sdk:adapter-tpn-mintegral-nonchina:6.4.69.7")
    implementation("com.mbridge.msdk.oversea:mbridge_android_sdk:16.9.11")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    //Chartboost
    implementation("com.anythink.sdk:adapter-tpn-chartboost:6.4.69")
    implementation("com.chartboost:chartboost-sdk:9.8.2")
    implementation("com.chartboost:chartboost-mediation-sdk:4.9.2")
    implementation("com.chartboost:chartboost-mediation-adapter-chartboost:4.9.8.1.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    //Tramini
//    implementation("com.anythink.sdk:tramini-plugin-tpn:6.4.69")

    //Max
    implementation("io.github.alex-only:max_adapter:1.2.5")


}