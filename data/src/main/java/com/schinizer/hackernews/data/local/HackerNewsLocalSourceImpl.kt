package com.schinizer.hackernews.data.local

import com.schinizer.hackernews.data.HackerNewsLocalSource
import com.schinizer.hackernews.data.local.room.ItemEntityDao
import com.schinizer.hackernews.data.local.room.TopStoryDao
import com.schinizer.hackernews.data.local.room.TopStoryEntity
import com.schinizer.hackernews.data.remote.Item
import com.schinizer.hackernews.data.remote.toEntity
import com.schinizer.hackernews.data.remote.toItem
import javax.inject.Inject

class HackerNewsLocalSourceImpl @Inject constructor(
    private val topStoryDao: TopStoryDao,
    private val itemEntityDao: ItemEntityDao
): HackerNewsLocalSource {
    override suspend fun top500Stories(): List<Int> {
        return topStoryDao.topStories(0, 500)
            .map { it.id }
    }

    override suspend fun saveTop500Stories(ids: List<Int>) {
        topStoryDao.dropAll()
        topStoryDao.saveItems(ids.map { TopStoryEntity(it) })
    }

    override suspend fun fetchItem(id: Int): Item {
        return itemEntityDao.queryItem(id)
            .toItem()
    }

    override suspend fun fetchItems(ids: List<Int>): List<Item> {
        val data = itemEntityDao.queryItems(ids).associateBy { it.id }
        return ids.map { data[it]?.toItem() ?: Item.Unsupported }
    }

    override suspend fun saveItems(items: List<Item>) {
        itemEntityDao.saveItems(items.mapNotNull { it.toEntity() })
    }
}