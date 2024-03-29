package com.schinizer.hackernews.ui.compose

import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.schinizer.hackernews.data.local.ItemState
import com.schinizer.hackernews.data.remote.Item
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ItemViewListStateless(
    itemStates: ImmutableList<ItemState>,
    modifier: Modifier = Modifier,
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onItemAttached: (id: Int) -> Unit = {},
    onItemDetached: (id: Int) -> Unit = {},
) {
    val lazyColumnState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)

    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyColumnState
        ) {
            items(
                items = itemStates,
                key = { it.id },
                contentType = { it.item }
            ) { (_, item, onClick) ->
                when(item) {
                    null -> {
                        ItemLoading()
                    }
                    is Item.Story -> {
                        val now = System.currentTimeMillis()
                        val timeAgo = DateUtils.getRelativeTimeSpanString(
                            item.time.seconds.inWholeMilliseconds,
                            now,
                            DateUtils.MINUTE_IN_MILLIS
                        )
                        val subtitle = "${item.score} points by ${item.by} | $timeAgo"

                        ItemView(
                            modifier = Modifier.animateItemPlacement(),
                            title = item.title,
                            subtitle = subtitle,
                            onClick = onClick
                        )
                    }
                    is Item.Job -> {
                        ItemView(
                            modifier = Modifier.animateItemPlacement(),
                            title = item.title,
                            subtitle = "",
                            onClick = onClick
                        )
                    }
                    is Item.Unsupported -> {
                        ItemUnsupported(
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    }

    val currentVisibleItemsInfo = remember { mutableMapOf<Int, LazyListItemInfo>() }

    LaunchedEffect(itemStates) {
        snapshotFlow { lazyColumnState.layoutInfo.visibleItemsInfo }
            .distinctUntilChangedBy { info -> info.map { it.index } }
            .collect { freshVisibleItemsInfo ->
                // remove existing views will result in views that detached
                for(info in freshVisibleItemsInfo) {
                    // If it wasnt in current visible items, means it is new
                    if(!currentVisibleItemsInfo.containsKey(info.index)) {
                        itemStates.getOrNull(info.index)?.let { onItemAttached(it.id) }
                    }

                    currentVisibleItemsInfo.remove(info.index)
                }

                // Cancel all items that doesnt need loading
                for((_, info) in currentVisibleItemsInfo) {
                    itemStates.getOrNull(info.index)?.let { onItemDetached(it.id) }
                }

                // Start fresh
                currentVisibleItemsInfo.clear()

                // Set the new visible items
                for(info in freshVisibleItemsInfo) {
                    currentVisibleItemsInfo[info.index] = info
                }
            }
    }
}

@Preview
@Composable
private fun ItemViewListPreview() {
    val itemStates = Array(8) { ItemState(it, null) }

    MaterialTheme(colorScheme = darkColorScheme()) {
        ItemViewListStateless(
            modifier = Modifier.fillMaxSize(),
            itemStates = persistentListOf(*itemStates)
        )
    }
}