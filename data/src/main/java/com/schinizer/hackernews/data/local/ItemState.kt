package com.schinizer.hackernews.data.local

import com.schinizer.hackernews.data.remote.Item

data class ItemState(
    val id: Int,
    val item: Item?,
    val onClick: () -> Unit
)