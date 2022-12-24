package com.schinizer.hackernews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.ui.compose.ItemViewListStateless

@Composable
fun ItemViewList(
    modifier: Modifier = Modifier,
    vm: HackerNewsViewModel = viewModel()
) {
    val itemStates by vm.dataFlow.collectAsState()
    val isLoading by vm.isLoadingFlow.collectAsState()

    ItemViewListStateless(
        modifier = modifier.testTag("ItemViewList"),
        itemStates = itemStates,
        refreshing = isLoading,
        onRefresh = vm::refreshData,
        onItemAttached = vm::loadItem,
        onItemDetached = vm::cancelLoadItem,
    )
}