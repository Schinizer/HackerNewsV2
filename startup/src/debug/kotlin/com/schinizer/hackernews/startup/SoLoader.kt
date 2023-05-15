package com.schinizer.hackernews.startup

import android.content.Context
import androidx.startup.Initializer
import com.facebook.soloader.SoLoader

class SoLoaderInitalizer: Initializer<Unit> {
    override fun create(context: Context) {
        SoLoader.init(context, false)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}