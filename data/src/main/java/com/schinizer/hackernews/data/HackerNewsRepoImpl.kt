package com.schinizer.hackernews.data

import com.schinizer.hackernews.data.remote.Item
import javax.inject.Inject

class HackerNewsRepoImpl @Inject constructor(
    private val local: HackerNewsLocalSource,
    private val remote: HackerNewsRemoteSource
): HackerNewsRepository {

    // Fetching goes:
    // 1: Get remote top500Stories and store it in local if there are content
    // 2: If remote is empty (due to errors), fallback to local top500Stories
    override suspend fun top500Stories(): List<Int> {
        return remote.top500Stories()
            .takeIf { it.isNotEmpty() }
            ?.also { local.saveTop500Stories(it) } ?: local.top500Stories()
    }

    // Fetching goes:
    // 1: Get remote item and store it in local if it is successful
    // 2: If remote failed, fallback to local item
    override suspend fun fetchItem(id: Int): Item? {
        return try {
            remote.fetchItem(id)
                ?.also { local.saveItem(it) }
        }
        catch (e: Exception) {
            local.fetchItem(id)
        }
    }
}