package com.schinizer.hackernews.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HackerNewsDBTest {
    private lateinit var userDao: ItemEntityDao
    private lateinit var db: HackerNewsDB

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HackerNewsDB::class.java).build()
        userDao = db.itemEntityDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }
}