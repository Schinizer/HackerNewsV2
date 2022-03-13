package com.schinizer.hackernews.ui

import com.airbnb.epoxy.EpoxyModelClass
import com.schinizer.hackernews.ui.databinding.LayoutItemUnsupportedBinding
import com.schinizer.hackernews.ui.helpers.ViewBindingEpoxyModelWithHolder

@EpoxyModelClass
abstract class ItemUnsupportedEpoxyModel : ViewBindingEpoxyModelWithHolder<LayoutItemUnsupportedBinding>() {
    override fun getDefaultLayout(): Int = R.layout.layout_item_unsupported
    override fun LayoutItemUnsupportedBinding.bind() = Unit
}