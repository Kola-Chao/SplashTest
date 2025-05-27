package com.buzz.shortnews.flashnews.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.appBackground
import com.buzz.shortnews.flashnews.core.designsystem.singleClick
import com.buzz.shortnews.flashnews.core.designsystem.theme.background_light
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF626973
import com.buzz.shortnews.flashnews.core.designsystem.theme.text_title
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import com.buzz.shortnews.flashnews.core.ui.commonString

@Composable
fun NetworkError(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(234.dp),
                painter = painterResource(id = commonDrawable.icon_load_fail),
                contentDescription = null
            )
            Text(
                text = stringResource(id = commonString.network_load_error),
                color = color_FF626973,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(40.dp)
                    .background(color = background_light, shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 34.dp)
                    .singleClick {
                        onRetry.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = commonString.Reload),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = text_title
                )
            }
        }
    }
}

@Preview
@Composable
fun NetworkErrorPreview() {
    NetworkError() {}
}