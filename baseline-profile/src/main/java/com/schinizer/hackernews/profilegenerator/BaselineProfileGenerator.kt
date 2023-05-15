package com.schinizer.hackernews.profilegenerator

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the Generate Baseline Profile run configuration,
 * or directly with `generateBaselineProfile` Gradle task:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 **/
@RequiresApi(Build.VERSION_CODES.P)
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collectBaselineProfile("com.schinizer.hackernews") {
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll
            // through your most important UI.

            // Start default activity for your app
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
    }
}