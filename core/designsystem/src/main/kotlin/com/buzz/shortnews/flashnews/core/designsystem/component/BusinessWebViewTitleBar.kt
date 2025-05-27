//package com.buzz.shortnews.flashnews.core.designsystem.component
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.basicMarquee
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.buzz.shortnews.flashnews.core.designsystem.R
//import com.buzz.shortnews.flashnews.core.designsystem.appBackground
//import com.buzz.shortnews.flashnews.core.designsystem.marquee
//
//@Composable
//fun BusinessWebViewTitleBar(
//    title: String,
//    countdownTime: Int = 0,
//    showCloseButton: Boolean = false,
//    onCloseClick: () -> Unit = {}
//) {
//    val backgroundBrush = SolidColor(Color(0x1AFFFFFF))
//    val borderBrush = SolidColor(Color(0x33FFFFFF))
//
//    Row(
//        modifier = Modifier
//            .appBackground(color = Color.Black)
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 10.dp)
//            .statusBarsPadding(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .width(220.dp)
//                .height(32.dp)
//                .fillMaxHeight()
//                .background(brush = backgroundBrush, shape = CircleShape)
//                .border(width = 0.5.dp, brush = borderBrush, shape = CircleShape)
//                .padding(horizontal = 16.dp),
//            contentAlignment = Alignment.CenterStart
//        ) {
//            // Title Text
//            Text(
//                modifier = Modifier.basicMarquee(),
//                text = title,
//                color = Color.White,
//                fontSize = 14.sp,
//                maxLines = 1,
//                textAlign = TextAlign.Start,
//            )
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Box(contentAlignment = Alignment.Center) {
//            countdownText?.let {
//                Box(
//                    modifier = Modifier
//                        .height(32.dp)
//                        .background(
//                            brush = backgroundBrush, shape = CircleShape
//                        )
//                        .border(width = 0.5.dp, brush = borderBrush, shape = CircleShape)
//                        .padding(horizontal = 12.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = it,
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        softWrap = false
//                    )
//                }
//            }
//
//            if (showCloseButton) {
//                IconButton(
//                    onClick = onCloseClick,
//                    modifier = Modifier.size(32.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(com.buzz.shortnews.flashnews.core.ui.R.drawable.common_back),
//                        contentDescription = "Close",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
//
//
//    }
//}
//
//@Preview
//@Composable
//fun BusinessWebViewTitleBarPreview() {
//    BusinessWebViewTitleBar(
//        title = "Sample Title 阿斯顿",
//        countdownText = "10s",
//        showCloseButton = true
//    )
//}