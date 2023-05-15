package com.schinizer.hackernews.startup

import android.content.Context
import androidx.startup.Initializer
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class FlipperClientInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        require(FlipperUtils.shouldEnableFlipper(context))

        val entryPoint = EntryPoints.get(context, FlipperEntryPoint::class.java)
        val client: FlipperClient = AndroidFlipperClient.getInstance(context)

        with(client) {
            addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            addPlugin(DatabasesFlipperPlugin(context))
            addPlugin(entryPoint.networkFlipperPlugin())
            start()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(SoLoaderInitalizer::class.java)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface FlipperEntryPoint {
        fun networkFlipperPlugin(): NetworkFlipperPlugin
    }
}