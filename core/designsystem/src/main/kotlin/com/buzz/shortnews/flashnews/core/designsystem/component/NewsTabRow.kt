package com.buzz.shortnews.flashnews.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import com.buzz.shortnews.flashnews.core.designsystem.tab.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buzz.shortnews.flashnews.core.designsystem.singleClick
import com.buzz.shortnews.flashnews.core.model.CategoryT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun NewsTabRow(
    modifier: Modifier = Modifier,
    tabs: List<CategoryT>,
    pagerState: PagerState,
    onTabSelected: (Int, CategoryT) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    // 监听 pagerState.currentPage 的变化，自动滚动到选中的 Tab
    LaunchedEffect(pagerState.currentPage) {
        coroutineScope.launch {
            // 等待布局完成
            while (lazyListState.layoutInfo.visibleItemsInfo.isEmpty()) {
                delay(10) // 延迟 10ms，等待布局完成
            }
            //获取总的宽度
            var totalWith = 0
            lazyListState.layoutInfo.visibleItemsInfo.forEachIndexed { index, lazyListItemInfo ->
                if (index != pagerState.currentPage) {
                    totalWith += lazyListItemInfo.size
                }
            }
            //滑动到正中间?-不完全是中间
            lazyListState.animateScrollToItem(pagerState.currentPage, -totalWith / 2)
            Timber.i("animateScrollToItem:${pagerState.currentPage}")
        }
    }

    Column(modifier = modifier
        .singleClick {}
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            state = lazyListState, // 传递 LazyListState
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(tabs) { index, category ->
                Tab(
                    modifier = Modifier
                        .wrapContentWidth(),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        onTabSelected.invoke(index, category)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .then(
                                if (pagerState.currentPage == index) {
                                    Modifier.background(Color.Black, RoundedCornerShape(6.dp))
                                } else {
                                    Modifier
                                        .border(
                                            0.5.dp,
                                            color = Color(0xFFE9ECF0),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .background(
                                            color = Color(0xFFF6F7F8),
                                            RoundedCornerShape(6.dp)
                                        )
                                }
                            )
                            .padding(12.dp, 5.dp)
                    ) {
                        Text(
                            modifier = Modifier,
                            text = category.display_name,
                            color = if (pagerState.currentPage == index) Color.White else Color.Black,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color(0xFFE9ECF0)
        )
    }
}

@Preview
@Composable
fun NewsTabRowPreview() {
    NewsTabRow(
        tabs = listOf(CategoryT("news", "news", 0)),
        pagerState = rememberPagerState(pageCount = { 7 }),
        onTabSelected = { _, _ -> }
    )
}