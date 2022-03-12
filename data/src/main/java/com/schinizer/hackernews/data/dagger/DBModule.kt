package com.schinizer.hackernews.data.dagger

import android.content.Context
import androidx.room.Room
import com.schinizer.hackernews.data.local.room.HackerNewsDB
import com.schinizer.hackernews.data.local.room.ItemEntityDao
import com.schinizer.hackernews.data.local.room.TopStoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class DBModule {
    private val dbName = "hackernews-db"

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): HackerNewsDB = Room.databaseBuilder(
        context,
        HackerNewsDB::class.java,
        dbName
    )
        .build()

    @Provides
    @Singleton
    fun provdeItemEntityDao(db: HackerNewsDB): ItemEntityDao = db.itemEntityDao()

    @Provides
    @Singleton
    fun provdeTopStoryDao(db: HackerNewsDB): TopStoryDao = db.topStoryDao()
}