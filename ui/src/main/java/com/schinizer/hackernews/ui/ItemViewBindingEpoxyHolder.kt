package com.schinizer.hackernews.ui

import com.airbnb.epoxy.EpoxyAttribute
import com.schinizer.hackernews.ui.databinding.LayoutItemViewBinding
import com.schinizer.hackernews.ui.helpers.ViewBindingEpoxyModelWithHolder

abstract class ItemViewBindingEpoxyHolder : ViewBindingEpoxyModelWithHolder<LayoutItemViewBinding>() {

    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: () -> Unit

    override fun LayoutItemViewBinding.bind() {
        title.text = this@ItemViewBindingEpoxyHolder.title
        root.setOnClickListener { onClick() }
    }

    override fun LayoutItemViewBinding.unbind() {
        // Don't leak listeners as this view goes back to the view pool
        title.setOnClickListener(null)
    }
}