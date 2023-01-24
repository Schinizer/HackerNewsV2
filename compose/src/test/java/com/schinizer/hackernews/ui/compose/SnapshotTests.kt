package com.schinizer.hackernews.ui.compose

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class SnapshotTests {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_6
    )
    @Test
    fun itemView() {
        paparazzi.snapshot {
            ItemViewPreview()
            ItemViewUnsupportedPreview()
            ItemLoadingPreview()
        }
    }

    @Test
    fun itemViewUnsupportedPreview() {
        paparazzi.snapshot {
            ItemViewUnsupportedPreview()
            ItemLoadingPreview()
        }
    }

    @Test
    fun itemLoadingPreview() {
        paparazzi.snapshot {
            ItemLoadingPreview()
        }
    }

    @Test
    fun itemViewList() {
        paparazzi.snapshot {
            ItemViewListPreview()
        }
    }
}