package com.schinizer.hackernews

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.data.dagger.DispatcherModule
import com.schinizer.hackernews.ui.compose.measureCompositionTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @DispatcherModule.UI
    lateinit var ui: CoroutineDispatcher

    private val viewModel by viewModels<HackerNewsViewModel>()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = when(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    true -> dynamicDarkColorScheme(LocalContext.current)
                    else -> darkColorScheme()
                }
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
                            .padding(contentPadding)
                            .measureCompositionTime("ItemViewList"),
                        vm = viewModel
                    )
                }

                LaunchedEffect(key1 = LocalLifecycleOwner.current) {
                    viewModel.actionFlow
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

        viewModel.refreshData()
    }

    private fun openInChrome(url: String) {
        val customTabIntent = CustomTabsIntent.Builder()
            .build()
        customTabIntent.launchUrl(this, Uri.parse(url))
    }
}