package com.schinizer.hackernews

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HackerNewsApplication: Application() {

    @Inject
    lateinit var networkPlugin: NetworkFlipperPlugin

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
            with(client) {
                addPlugin(InspectorFlipperPlugin(this@HackerNewsApplication, DescriptorMapping.withDefaults()))
                addPlugin(DatabasesFlipperPlugin(this@HackerNewsApplication))
                addPlugin(networkPlugin)
                start()
            }
        }
    }
}