package com.schinizer.hackernews.ui

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.schinizer.hackernews.ui.databinding.LayoutItemLoadingBinding
import com.schinizer.hackernews.ui.helpers.ViewBindingEpoxyModelWithHolder
import com.schinizer.hackernews.ui.helpers.ViewBindingHolder

@EpoxyModelClass
abstract class ItemLoadingEpoxyModel : ViewBindingEpoxyModelWithHolder<LayoutItemLoadingBinding>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onAttached: (() -> Unit)? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onDetached: (() -> Unit)? = null

    override fun getDefaultLayout(): Int = R.layout.layout_item_loading

    override fun LayoutItemLoadingBinding.bind() {
        shimmer.startShimmer()
    }

    override fun LayoutItemLoadingBinding.unbind() {
        shimmer.stopShimmer()
    }

    override fun onViewAttachedToWindow(holder: ViewBindingHolder) {
        onAttached?.invoke()
    }

    override fun onViewDetachedFromWindow(holder: ViewBindingHolder) {
        onDetached?.invoke()
    }
}