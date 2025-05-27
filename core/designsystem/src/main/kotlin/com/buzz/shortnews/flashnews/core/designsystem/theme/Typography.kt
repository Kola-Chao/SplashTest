package com.buzz.shortnews.flashnews.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    labelLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontSize = 14.sp
    )
)

val CustomGameStyleTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CustomGameStyleFontFamily,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CustomGameStyleFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    labelLarge = TextStyle(
        fontFamily = CustomGameStyleFontFamily,
        fontSize = 14.sp
    )
)
