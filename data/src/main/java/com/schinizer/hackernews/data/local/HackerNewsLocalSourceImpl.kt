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
        return topStoryDao.topStories(500, 0)
            .map { it.id }
    }

    // To save the stories ids, it is safe to drop the stale ones (i.e everything)
    // Here we write the id & order into the DB
    override suspend fun saveTop500Stories(ids: List<Int>) {
        topStoryDao.dropAll()
        topStoryDao.saveItems(ids.mapIndexed { index, i -> TopStoryEntity(i, index) })
    }

    override suspend fun fetchItem(id: Int): Item? {
        return itemEntityDao.queryItem(id)
            ?.toItem()
    }

    override suspend fun saveItems(items: List<Item>) {
        itemEntityDao.saveItems(items.mapNotNull { it.toEntity() })
    }

    override suspend fun saveItem(item: Item) {
        item.toEntity()
            ?.let {  itemEntityDao.saveItem(it) }
    }
}