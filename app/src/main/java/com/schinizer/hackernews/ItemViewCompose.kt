package com.schinizer.hackernews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.ui.compose.ItemViewListStateless

@Composable
fun ItemViewList(
    modifier: Modifier = Modifier,
    vm: HackerNewsViewModel = viewModel()
) {
    val itemStates by vm.dataFlow.collectAsState()

    ItemViewListStateless(
        modifier = modifier,
        itemStates = itemStates,
        onItemAttached = vm::loadItem,
        onItemDetached = vm::cancelLoadItem,
    )
}