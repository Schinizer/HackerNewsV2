package com.schinizer.hackernews.data.remote.retrofit

import com.schinizer.hackernews.data.remote.Item
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsAPI {
    @GET("topstories.json")
    suspend fun top500Stories(): List<Int>

    @GET("item/{id}.json")
    suspend fun fetchItem(@Path("id") id: Int): Item?
}