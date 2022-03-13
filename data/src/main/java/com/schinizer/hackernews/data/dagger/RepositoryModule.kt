package com.schinizer.hackernews.data.dagger

import com.schinizer.hackernews.data.HackerNewsLocalSource
import com.schinizer.hackernews.data.HackerNewsRemoteSource
import com.schinizer.hackernews.data.HackerNewsRepoImpl
import com.schinizer.hackernews.data.HackerNewsRepository
import com.schinizer.hackernews.data.local.HackerNewsLocalSourceImpl
import com.schinizer.hackernews.data.remote.HackerNewsRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun localSource(impl: HackerNewsLocalSourceImpl): HackerNewsLocalSource

    @Binds
    abstract fun remoteSource(impl: HackerNewsRemoteSourceImpl): HackerNewsRemoteSource

    @Binds
    abstract fun repo(impl: HackerNewsRepoImpl): HackerNewsRepository
}