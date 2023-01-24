package com.schinizer.hackernews.data.local

import com.schinizer.hackernews.data.remote.Item

internal val noop = {}
data class ItemState(
    val id: Int,
    val item: Item? = null,
    val onClick: () -> Unit = noop
)