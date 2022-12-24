package com.schinizer.hackernews

import android.net.Uri
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.data.dagger.DispatcherModule
import com.schinizer.hackernews.databinding.ActivityMainBinding
import com.schinizer.hackernews.ui.compose.measureCompositionTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @DispatcherModule.UI
    lateinit var ui: CoroutineDispatcher

    private val binding by viewBinding<ActivityMainBinding>()
    private val viewModel by viewModels<HackerNewsViewModel>()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme()
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics {
                            // Allows to use testTag() for UiAutomator's resource-id.
                            // It can be enabled high in the compose hierarchy,
                            // so that it's enabled for the whole subtree
                            testTagsAsResourceId = true
                        },
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
            }
        }

        lifecycleScope.launch(ui) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionFlow
                    .collect {
                        when (it) {
                            is HackerNewsViewModel.OpenBrowser -> openInChrome(it.url)
                            is HackerNewsViewModel.ShowUnsupportedSnackBar -> Snackbar.make(
                                this@MainActivity,
                                binding.recyclerView,
                                "No support yet for this item :)",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
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