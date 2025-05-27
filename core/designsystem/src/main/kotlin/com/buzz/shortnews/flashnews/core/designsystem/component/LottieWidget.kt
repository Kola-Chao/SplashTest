package com.buzz.shortnews.flashnews.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieWidget(
    modifier: Modifier = Modifier,
    assetName: String,
    imageAssetsFolder: String? = ""
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset(assetName),
        imageAssetsFolder = imageAssetsFolder
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(
        modifier = modifier
            .clickable(enabled = false) {},
        composition = composition,
        progress = { progress },
    )
}