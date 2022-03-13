package com.schinizer.hackernews.data.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UI

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IO

    @Provides
    @UI
    fun ui(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @IO
    fun io(): CoroutineDispatcher = Dispatchers.IO
}