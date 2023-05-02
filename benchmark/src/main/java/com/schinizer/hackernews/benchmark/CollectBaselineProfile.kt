package com.schinizer.hackernews.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test

@RequiresApi(Build.VERSION_CODES.P)
class CollectBaselineProfile {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collectBaselineProfile(
        packageName = TARGET_PACKAGE,
        profileBlock = {
            startActivityAndWait()
            with(device) {
                /**
                 * Compose does not have view IDs so we cannot directly access composables from UiAutomator.
                 * To access a composable we need to set:
                 * 1) Modifier.semantics { testTagsAsResourceId = true } once, high in the compose hierarchy
                 * 2) Add Modifier.testTag("someIdentifier") to all of the composables you want to access
                 *
                 * Once done that, we can access the composable using By.res("someIdentifier")
                 */
                val column = device.findObject(By.res("ItemViewList"))

                // Set gesture margin to avoid triggering gesture navigation
                // with input events from automation.
                column.setGestureMargin(device.displayWidth / 5)

                // Scroll down several times
                repeat(3) { column.fling(Direction.DOWN) }
                repeat(3) { column.fling(Direction.UP) }

                pressBack()
            }
        }
    )
}