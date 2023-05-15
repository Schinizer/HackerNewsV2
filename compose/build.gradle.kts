plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.blp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.schinizer.hackernews.ui.compose"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
    automaticGenerationDuringBuild = true
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