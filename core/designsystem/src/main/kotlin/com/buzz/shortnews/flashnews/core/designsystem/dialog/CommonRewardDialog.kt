package com.buzz.shortnews.flashnews.core.designsystem.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.buzz.shortnews.flashnews.core.designsystem.component.CommonCoinBubble

@Composable
fun CommonCoinBubbleContainer(
    modifier: Modifier = Modifier,
    showCoinBubble: MutableState<Int>
) {
    if (showCoinBubble.value > 0) {
        CommonCoinBubble(
            modifier = modifier,
            coin = showCoinBubble.value
        )
    }
}

@Composable
fun RewardDialogContainer(
    balance: String,
    isShowRewardDialog: MutableState<Pair<Boolean, Int>>,
    receivedCallback: (Int) -> Unit
) {
    if (isShowRewardDialog.value.first) {
        RewardDialog(
            balance = balance,
            coin = isShowRewardDialog.value.second,
        ) {
            receivedCallback.invoke(isShowRewardDialog.value.second)
            isShowRewardDialog.value = false to 0
        }
    }
}