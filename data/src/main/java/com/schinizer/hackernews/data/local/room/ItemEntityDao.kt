package com.schinizer.hackernews.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ItemEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItems(entities: List<ItemEntity>)

    @Query("SELECT * FROM item where id=:id")
    suspend fun queryItem(id: Int): ItemEntity

    @Query("SELECT * FROM item where id IN (:ids)")
    suspend fun queryItems(ids: List<Int>): List<ItemEntity>
}