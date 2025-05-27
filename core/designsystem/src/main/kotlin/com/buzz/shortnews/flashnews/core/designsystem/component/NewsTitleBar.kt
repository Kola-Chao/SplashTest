package com.buzz.shortnews.flashnews.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.appBackground
import com.buzz.shortnews.flashnews.core.designsystem.singleClick

@Composable
fun NewsTitleBar(
    modifier: Modifier = Modifier,
    title: String,
    leftActionRes: Int? = null,
    rightActionRes: Int? = null,
    onLeftActionClick: () -> Unit = {},
    onRightActionClick: () -> Unit = {}
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Column(
        modifier = modifier
            .appBackground(color = Color.White)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(statusBarHeight))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            if (leftActionRes != null) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .singleClick {
                            onLeftActionClick.invoke()
                        },
                    painter = painterResource(id = leftActionRes),
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF171719),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )

            if (rightActionRes != null) {
                Image(
                    modifier = Modifier.size(24.dp).singleClick(
                        interactionSource = null,
                        indication = null
                    ) {
                        onRightActionClick.invoke()
                    },
                    painter = painterResource(id = rightActionRes),
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview
@Composable
fun NewsTitleBarPreview() {
    NewsTitleBar(title = "H5")
}