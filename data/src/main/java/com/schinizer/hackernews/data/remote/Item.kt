package com.schinizer.hackernews.data.remote

import com.schinizer.hackernews.data.local.room.ItemEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.zacsweers.moshix.sealed.annotations.DefaultObject
import dev.zacsweers.moshix.sealed.annotations.TypeLabel

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed interface Item {
    @TypeLabel("story")
    @JsonClass(generateAdapter = true)
    data class Story(
        @Json(name = "id") val id: Int,
        @Json(name = "type") val type: String,
        @Json(name = "title") val title: String,
        @Json(name = "url") val url: String
    ): Item

    @TypeLabel("job")
    @JsonClass(generateAdapter = true)
    data class Job(
        @Json(name = "id") val id: Int,
        @Json(name = "type") val type: String,
        @Json(name = "title") val title: String,
        @Json(name = "text") val text: String
    ): Item

    @DefaultObject
    object Unsupported: Item
}

// Converter from entity back to item
fun ItemEntity.toItem(): Item {
    return when(type) {
        "story" -> Item.Story(id, type, title, url)
        "job" -> Item.Job(id, type, title, text)
        else -> Item.Unsupported
    }
}

fun Item.toEntity(): ItemEntity? {
    return when(this) {
        is Item.Job -> ItemEntity(id, type, title, "", text)
        is Item.Story -> ItemEntity(id, type, title, url, "")
        else -> null
    }
}