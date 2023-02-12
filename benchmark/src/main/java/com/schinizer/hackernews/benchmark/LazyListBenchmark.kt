package com.schinizer.hackernews.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LazyListBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollComposeList() {
        benchmarkRule.measureRepeated(
            // [START_EXCLUDE]
            packageName = TARGET_PACKAGE,
            metrics = listOf(FrameTimingMetric()),
            // Try switching to different compilation modes to see the effect
            // it has on frame timing metrics.
            compilationMode = CompilationMode.None(),
            startupMode = StartupMode.WARM, // restarts activity each iteration
            iterations = DEFAULT_ITERATIONS,
            // [END_EXCLUDE]
            setupBlock = {
                // Before starting to measure, navigate to the UI to be measured
                val intent = Intent("$TARGET_PACKAGE.ComposeActivity")
                startActivityAndWait(intent)
            }
        ) {
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
        }
    }
}