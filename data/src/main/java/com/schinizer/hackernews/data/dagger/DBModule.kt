package com.schinizer.hackernews.data.dagger

import android.content.Context
import androidx.room.Room
import com.schinizer.hackernews.data.local.room.HackerNewsDB
import com.schinizer.hackernews.data.local.room.ItemEntityDao
import com.schinizer.hackernews.data.local.room.TopStoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    private const val dbName = "hackernews-db"

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): HackerNewsDB = Room.databaseBuilder(
        context,
        HackerNewsDB::class.java,
        dbName
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provdeItemEntityDao(db: HackerNewsDB): ItemEntityDao = db.itemEntityDao()

    @Provides
    @Singleton
    fun provdeTopStoryDao(db: HackerNewsDB): TopStoryDao = db.topStoryDao()
}