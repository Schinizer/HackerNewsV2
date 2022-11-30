package com.schinizer.hackernews.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.schinizer.hackernews.ui.compose.databinding.LayoutItemViewBinding
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ItemView(
    modifier: Modifier = Modifier,
    title: String = "There's no speed limit (2009)",
    subtitle: String = "250 points by melling | 4 days ago",
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(all = 16.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = subtitle,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemViewBridge(
    modifier: Modifier = Modifier,
    title: String = "There's no speed limit (2009)",
    subtitle: String = "250 points by melling | 4 days ago",
    onClick: () -> Unit = {}
) {
    AndroidViewBinding(
        modifier = modifier
            .clickable(onClick = onClick),
        factory = LayoutItemViewBinding::inflate
    ) {
        this.title.text = title
        this.subtitle.text = subtitle
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemUnsupported(
    modifier: Modifier = Modifier,
    title: String = "Content not supported!"
) {
    Text(
        modifier = modifier
            .padding(all = 16.dp),
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleLarge,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
    ) {
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

        val thickness = 22.dp
        val color = MaterialTheme.colorScheme.tertiary
        val spacingHeight = 8.dp

        Divider(color = color, thickness = thickness, modifier = Modifier.shimmer(shimmerInstance))
        Spacer(modifier = Modifier.height(spacingHeight))
        Divider(color = color, thickness = thickness, modifier = Modifier.shimmer(shimmerInstance))
        Spacer(modifier = Modifier.height(spacingHeight))
        Divider(color = color, thickness = thickness, modifier = Modifier.shimmer(shimmerInstance))
    }
}

@Preview
@Composable
fun ItemViewPreview() {
    MaterialTheme {
        ItemView(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ItemViewUnsupportedPreview() {
    MaterialTheme {
        ItemUnsupported(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ItemLoadingPreview() {
    MaterialTheme {
        ItemLoading(
            modifier = Modifier.fillMaxWidth()
        )
    }
}