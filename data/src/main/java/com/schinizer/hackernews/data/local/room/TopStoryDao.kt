package com.schinizer.hackernews.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItems(entities: List<TopStoryEntity>)

    @Query("SELECT * FROM topstory ORDER BY `index` ASC LIMIT :limit OFFSET :offset")
    suspend fun topStories(limit: Int, offset: Int): List<TopStoryEntity>

    @Query("DELETE FROM topstory")
    suspend fun dropAll()
}