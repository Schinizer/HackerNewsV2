package com.schinizer.hackernews

import android.text.format.DateUtils
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import com.airbnb.epoxy.AsyncEpoxyController
import com.schinizer.hackernews.business.HackerNewsViewModel
import com.schinizer.hackernews.data.remote.Item
import com.schinizer.hackernews.ui.ItemLoadingEpoxyModel_
import com.schinizer.hackernews.ui.ItemUnsupportedEpoxyModel_
import com.schinizer.hackernews.ui.ItemViewEpoxyModel_
import java.util.concurrent.TimeUnit

class ItemEpoxyController(
    private val viewModel: HackerNewsViewModel
) : AsyncEpoxyController() {
    var data = listOf<HackerNewsViewModel.ItemState>()
        set(value) {
            field = value
            requestDelayedModelBuild(200)
        }

    override fun buildModels() {
        data.forEach { entry ->
            val item = entry.item
            val id = entry.id
            when (item) {
                null -> {
                    ItemLoadingEpoxyModel_()
                        .id(id)
                        .onAttached {
                            viewModel.loadItem(id)
                        }
                        .onDetached {
                            viewModel.cancelLoadItem(id)
                        }
                        .addTo(this)
                }
                is Item.Story -> {
                    val now = System.currentTimeMillis()
                    val timeAgo = DateUtils.getRelativeTimeSpanString(
                        TimeUnit.SECONDS.toMillis(item.time),
                        now,
                        MINUTE_IN_MILLIS
                    )
                    ItemViewEpoxyModel_()
                        .id(id)
                        .title(item.title)
                        .subtitle("${item.score} points by ${item.by} $timeAgo")
                        .onClick {
                            viewModel.openItem(id)
                        }
                        .addTo(this)
                }

                is Item.Job -> {
                    ItemViewEpoxyModel_()
                        .id(id)
                        .title(item.title)
                        .onClick {
                            viewModel.openItem(id)
                        }
                        .addTo(this)
                }
                is Item.Unsupported -> {
                    ItemUnsupportedEpoxyModel_()
                        .id(id)
                        .addTo(this)
                }
            }
        }
    }
}
