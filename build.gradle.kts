import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.blp) apply false
}

subprojects {
    tasks.withType<KotlinCompile>()
            .matching {
                !it.name.startsWith("kapt")
            }
            .configureEach {
                kotlinOptions {
                    if (project.findProperty("hackernews.enableComposeCompilerReports") == "true") {
                        freeCompilerArgs = freeCompilerArgs + listOf(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_metrics/metric",
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_metrics/report",
                        )
                    }
                }
            }
}
