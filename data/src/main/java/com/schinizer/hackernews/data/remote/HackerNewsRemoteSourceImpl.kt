package com.schinizer.hackernews.data.remote

import com.schinizer.hackernews.data.HackerNewsRemoteSource
import com.schinizer.hackernews.data.remote.retrofit.HackerNewsAPI
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

    override suspend fun fetchItem(id: Int): Item? {
        return hackerNewsAPI.fetchItem(id)
    }
}