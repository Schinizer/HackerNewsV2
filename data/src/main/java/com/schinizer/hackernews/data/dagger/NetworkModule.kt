package com.schinizer.hackernews.data.dagger

import com.schinizer.hackernews.data.remote.retrofit.HackerNewsAPI
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class NetworkModule {

    private val baseURL = "https://hacker-news.firebaseio.com/v0/"

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .build()


    @Provides
    @Singleton
    fun provideHackerNewsAPI(client: OkHttpClient, moshi: Moshi): HackerNewsAPI = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(HackerNewsAPI::class.java)
}