package com.schinizer.hackernews.data.dagger

import android.app.Application
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.schinizer.hackernews.data.remote.retrofit.HackerNewsAPI
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val baseURL = "https://hacker-news.firebaseio.com/v0/"

    @Provides
    @Singleton
    fun httpCache(app: Application) = Cache(
        directory = File(app.cacheDir, "http_cache"),
        // $0.05 worth of phone storage in 2020
        maxSize = 50L * 1024L * 1024L // 50 MiB
    )

    @Provides
    @Singleton
    fun okhttpClient(httpCache: Cache, flipperInterceptor: FlipperOkhttpInterceptor): OkHttpClient = OkHttpClient.Builder()
        .cache(httpCache)
        .addNetworkInterceptor(flipperInterceptor)
        .build()

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .build()


    @Provides
    @Singleton
    fun hackerNewsAPI(client: OkHttpClient, moshi: Moshi): HackerNewsAPI = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(HackerNewsAPI::class.java)
}