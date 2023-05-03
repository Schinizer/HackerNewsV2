package com.schinizer.hackernews.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            .clickable(
                onClick = onClick
            )
            .padding(all = 16.dp)
    ) {
        Text(
            text = title,
            overflow = TextOverflow.Ellipsis,
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

@Composable
fun ItemLoading(
    modifier: Modifier = Modifier
) {
    val thickness = 22.dp
    val color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f)
    val spacingHeight = 8.dp

    Column(
        modifier = modifier
            .padding(all = 16.dp)
    ) {
        Divider(thickness = thickness, color = color)
        Spacer(modifier = Modifier.height(spacingHeight))
        Divider(thickness = thickness, color = color)
        Spacer(modifier = Modifier.height(spacingHeight))
        Divider(thickness = thickness, color = color)
    }
}

@Preview
@Composable
private fun ItemViewPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ItemView(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ItemViewUnsupportedPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ItemUnsupported(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ItemLoadingPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ItemLoading(
            modifier = Modifier.fillMaxWidth()
        )
    }
}