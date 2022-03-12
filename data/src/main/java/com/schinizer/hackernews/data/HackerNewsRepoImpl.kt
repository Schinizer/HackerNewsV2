package com.schinizer.hackernews.data

import com.schinizer.hackernews.data.remote.Item
import javax.inject.Inject

class HackerNewsRepoImpl @Inject constructor(
    private val local: HackerNewsLocalSource,
    private val remote: HackerNewsRemoteSource
): HackerNewsRepository {
    override suspend fun top500Stories(): List<Int> {
        return remote.top500Stories()
            .takeIf { it.isNotEmpty() }
            ?.also { local.saveTop500Stories(it) } ?: local.top500Stories()
    }

    override suspend fun fetchItems(ids: List<Int>): List<Item> {
        return remote.fetchItems(ids)
            .takeIf { it.isNotEmpty() }
            ?.also { local.saveItems(it) } ?: local.fetchItems(ids)
    }
}