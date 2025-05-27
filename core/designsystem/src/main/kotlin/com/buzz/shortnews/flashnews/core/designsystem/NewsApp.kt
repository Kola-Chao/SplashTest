package com.buzz.shortnews.flashnews.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.buzz.shortnews.flashnews.core.designsystem.theme.background_light

@Composable
fun NewsApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background_light)
    ) {
        Text(text = "Hello")
    }
}