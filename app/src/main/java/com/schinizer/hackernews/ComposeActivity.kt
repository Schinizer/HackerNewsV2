package com.schinizer.hackernews

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HackerNewsApp(openInChrome = ::openInChrome)
        }
    }

    private fun openInChrome(url: String) {
        val customTabIntent = CustomTabsIntent.Builder()
            .build()
        customTabIntent.launchUrl(this, Uri.parse(url))
    }
}