package com.buzz.shortnews.flashnews.core.designsystem.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.singleClick
import com.buzz.shortnews.flashnews.core.designsystem.theme.border_color
import com.buzz.shortnews.flashnews.core.designsystem.theme.color_FF15141F
import com.buzz.shortnews.flashnews.core.designsystem.theme.disabled_color
import com.buzz.shortnews.flashnews.core.ui.commonDrawable
import com.buzz.shortnews.flashnews.core.ui.commonString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsMoreDialog(
    onDismissRequest: () -> Unit,
    onNotInterested: () -> Unit,
    onNewsReportClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = Color.White,
        shape = RoundedCornerShape(0.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 20.dp)
                    .size(40.dp, 5.dp)
                    .background(disabled_color, RoundedCornerShape(99.dp))
            )
        }
    ) {
        MoreItem(
            icon = commonDrawable.icon_not_interested,
            title = stringResource(id = commonString.not_interested),
            clickCallback = {
                onNotInterested.invoke()
                onDismissRequest.invoke()
            }
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 20.dp),
            thickness = 0.5.dp,
            color = border_color
        )
        MoreItem(
            icon = commonDrawable.icon_report,
            title = stringResource(id = commonString.report_this_content),
            clickCallback = {
                onNewsReportClick.invoke()
                onDismissRequest.invoke()
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun MoreItem(
    icon: Int, title: String,
    clickCallback: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(44.dp)
            .singleClick { clickCallback.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(44.dp),
            painter = painterResource(id = icon), contentDescription = null
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = color_FF15141F
        )
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = commonDrawable.ic_more_arrow_right),
            contentDescription = null
        )
    }
}