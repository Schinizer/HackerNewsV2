package com.schinizer.hackernews.data.dagger

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlipperModule {
    @Provides
    @Singleton
    fun networkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()

    @Provides
    @Singleton
    fun flipperOkhttpInterceptor(plugin: NetworkFlipperPlugin): FlipperOkhttpInterceptor = FlipperOkhttpInterceptor(plugin)
}