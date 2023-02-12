package com.schinizer.hackernews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.viewmodel.compose.viewModel
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.ui.compose.ItemViewListStateless

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun HackerNewsApp(
    vm: HackerNewsViewModel = viewModel(),
    openInChrome: (String) -> Unit
) {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    // Allows to use testTag() for UiAutomator's resource-id.
                    // It can be enabled high in the compose hierarchy,
                    // so that it's enabled for the whole subtree
                    testTagsAsResourceId = true
                },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                )
            }
        ) { contentPadding ->
            ItemViewList(
                modifier = Modifier
                    .padding(contentPadding),
                vm = vm
            )
        }

        LaunchedEffect(key1 = LocalLifecycleOwner.current) {
            vm.refreshData()
            vm.actionFlow
                .collect {
                    when (it) {
                        is HackerNewsViewModel.OpenBrowser -> openInChrome(it.url)
                        is HackerNewsViewModel.ShowUnsupportedSnackBar -> snackbarHostState.showSnackbar(
                            message = "No support yet for this item :)"
                        )
                    }
                }
        }
    }
}

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