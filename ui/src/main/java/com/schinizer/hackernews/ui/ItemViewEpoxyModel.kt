package com.schinizer.hackernews.ui

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.schinizer.hackernews.ui.databinding.LayoutItemViewBinding
import com.schinizer.hackernews.ui.helpers.ViewBindingEpoxyModelWithHolder

@EpoxyModelClass
abstract class ItemViewEpoxyModel : ViewBindingEpoxyModelWithHolder<LayoutItemViewBinding>() {

    @EpoxyAttribute var title: String? = null
    @EpoxyAttribute var subtitle: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClick: (() -> Unit)? = null

    override fun getDefaultLayout(): Int = R.layout.layout_item_view

    override fun LayoutItemViewBinding.bind() {
        title.isClickable = false
        subtitle.isClickable = false
        this@ItemViewEpoxyModel.title?.let {
            title.text = it
        }
        this@ItemViewEpoxyModel.subtitle?.let { subtitle.text = it }
        root.setOnClickListener { onClick?.invoke() }
    }

    override fun LayoutItemViewBinding.unbind() {
        // Don't leak listeners as this view goes back to the view pool
        title.setOnClickListener(null)
    }
}