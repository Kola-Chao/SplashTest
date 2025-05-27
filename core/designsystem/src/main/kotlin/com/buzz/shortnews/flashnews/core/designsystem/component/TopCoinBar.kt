package com.buzz.shortnews.flashnews.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.theme.CustomGameStyleTypography
import com.buzz.shortnews.flashnews.core.designsystem.theme.NewsTheme
import com.buzz.shortnews.flashnews.core.ui.commonDrawable

@Composable
fun TopCoinBar(
    modifier: Modifier = Modifier,
    coinState: State<Int>,
    coinOffset: (Offset) -> Unit = {}
) {
    NewsTheme(typography = CustomGameStyleTypography) {
        Row(
            modifier = modifier
                .height(30.dp)
                .background(color = Color(0x52000000), shape = RoundedCornerShape(99.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(26.dp)
                    .onGloballyPositioned {
                        coinOffset.invoke(it.positionInWindow())
                    },
                painter = painterResource(id = commonDrawable.common_coin),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 12.dp),
                text = coinState.value.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun TopCoinBarPreView() {
    TopCoinBar(coinState = mutableIntStateOf(10000))
}