package com.schinizer.hackernews

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.data.dagger.DispatcherModule
import com.schinizer.hackernews.databinding.ActivityMainBinding
import com.schinizer.hackernews.helpers.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @DispatcherModule.UI
    lateinit var ui: CoroutineDispatcher

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<HackerNewsViewModel>()

    private val controller by lazy { ItemEpoxyController(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        with(binding) {
            setContentView(root)
            setSupportActionBar(toolbar)

            recyclerView.setController(controller)

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshData()
            }

            fab.setOnClickListener {
                recyclerView.scrollToPosition(0)
            }
        }

        lifecycleScope.launch(ui) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow
                    .collect {
                        controller.data = it
                    }
            }
        }

        lifecycleScope.launch(ui) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingFlow
                    .collect {
                        if(it) return@collect
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
            }
        }

        lifecycleScope.launch(ui) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionFlow
                    .collect {
                        when (it) {
                            is HackerNewsViewModel.OpenBrowser -> openInChrome(it.url)
                            is HackerNewsViewModel.ShowUnsupportedToast -> Snackbar.make(
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
    }

    private fun openInChrome(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setPackage("com.android.chrome")
        }
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null)
            startActivity(intent)
        }
    }
}