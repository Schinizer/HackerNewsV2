package com.schinizer.hackernews.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Flattened Item Entity for easier storage & query
 */
@Entity(tableName = "item")
data class ItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,

    @ColumnInfo(name = "by") val by: String,
    @ColumnInfo(name = "descendants") val descendants: Int,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "text") val text: String?
)