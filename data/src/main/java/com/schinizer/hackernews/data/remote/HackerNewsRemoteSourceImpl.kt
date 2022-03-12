package com.schinizer.hackernews.data.remote

import com.schinizer.hackernews.data.HackerNewsRemoteSource
import com.schinizer.hackernews.data.remote.retrofit.HackerNewsAPI
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class HackerNewsRemoteSourceImpl @Inject constructor(
    private val hackerNewsAPI: HackerNewsAPI
): HackerNewsRemoteSource {
    override suspend fun top500Stories(): List<Int> {
        return try {
            hackerNewsAPI.top500Stories()
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun fetchItems(ids: List<Int>): List<Item> = coroutineScope {
        ids.map {
            async {
                try {
                    hackerNewsAPI.fetchItem(it)
                } catch (e: Exception) {
                    null
                }
            }
        }
            .awaitAll()
            .filterNotNull()
    }
}