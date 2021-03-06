package com.schinizer.hackernews.data

import com.schinizer.hackernews.data.remote.Item

interface HackerNewsRepository {
    suspend fun top500Stories(): List<Int>
    suspend fun fetchItem(id: Int): Item?
}

interface HackerNewsLocalSource: HackerNewsRepository {
    suspend fun saveTop500Stories(ids: List<Int>)
    suspend fun saveItems(items: List<Item>)
    suspend fun saveItem(item: Item)
}

interface HackerNewsRemoteSource: HackerNewsRepository