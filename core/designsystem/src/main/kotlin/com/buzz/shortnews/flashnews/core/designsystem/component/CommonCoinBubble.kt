package com.buzz.shortnews.flashnews.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.theme.CustomGameStyleTypography
import com.buzz.shortnews.flashnews.core.designsystem.theme.NewsTheme
import com.buzz.shortnews.flashnews.core.ui.commonDrawable

@Composable
fun TriangleShape(modifier: Modifier, color: Color) {
    Canvas(
        modifier = modifier
            .size(10.dp, 5.dp)
            .background(color = Color(0x00000000))
    ) {
        val path = Path().apply {
            // 从底部左侧开始
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        drawPath(
            path = path,
            color = color,
            style = Fill
        )
    }
}

@Composable
fun CommonCoinBubble(modifier: Modifier = Modifier, coin: Int) {
    NewsTheme(typography = CustomGameStyleTypography) {
        Column(
            modifier = modifier
                .wrapContentSize()
        ) {
            TriangleShape(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .graphicsLayer {
                        translationY = 1.dp.value
                    },
                color = Color(0xFFFF5C1C)
            )
            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF5C1C),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(start = 4.dp, top = 2.dp, bottom = 2.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(id = commonDrawable.ic_bubble_coin),
                    contentDescription = null
                )
                Text(
                    text = "+$coin",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Preview
@Composable
fun CoinBubblePreview() {
    CommonCoinBubble(coin = 2000)
}