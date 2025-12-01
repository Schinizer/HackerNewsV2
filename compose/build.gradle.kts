import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.blp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.schinizer.hackernews.ui.compose"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    buildFeatures {
        compose = true
    }
}

baselineProfile {
    filter {
        include("com.schinizer.hackernews.ui.compose.**")
    }

    // Note that these are the default settings, just reported here to make it explicit.
    // `automaticGenerationDuringBuild` has to be off otherwise assembling release on CI would
    // trigger baseline profile generation and integration tests on device.
    saveInSrc = true
    //automaticGenerationDuringBuild = true
}

dependencies {
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.compose.bom))

    implementation(libs.compose.ui)
    // Tooling support (Previews, etc.)
    implementation(libs.compose.ui.tooling)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.compose.foundation)
    // Material Design
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    // Integration with activities
    implementation(libs.androidx.activity.compose)
    // Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.compose.shimmer)

    implementation(libs.kotlinx.collections.immutable)

    lintChecks(libs.compose.linter)

    baselineProfile(project(":baseline-profile"))

    // UI Tests
    testImplementation(libs.bundles.unitTest)
    androidTestImplementation(libs.bundles.integrationTest)
}