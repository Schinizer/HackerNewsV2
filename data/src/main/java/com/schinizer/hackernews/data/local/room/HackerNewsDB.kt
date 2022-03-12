package com.schinizer.hackernews.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TopStoryEntity::class, ItemEntity::class], version = 1)
abstract class HackerNewsDB: RoomDatabase() {
    abstract fun itemEntityDao(): ItemEntityDao
    abstract fun topStoryDao(): TopStoryDao
}