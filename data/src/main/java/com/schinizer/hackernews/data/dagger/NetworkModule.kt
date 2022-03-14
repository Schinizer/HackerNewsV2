package com.schinizer.hackernews.data.dagger

import android.app.Application
import com.schinizer.hackernews.data.remote.retrofit.HackerNewsAPI
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Qualifier
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
    fun okhttpClient(
        httpCache: Cache,
        @NetworkInterceptor networkInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(httpCache)
        .apply {
            networkInterceptors.forEach { addNetworkInterceptor(it) }
        }
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

    @Provides
    @NetworkInterceptor
    @ElementsIntoSet
    fun emptyNetworkInterceptors(): Set<Interceptor> = emptySet()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NetworkInterceptor
}