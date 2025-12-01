import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
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
    namespace = "com.schinizer.hackernews.data"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.kotlinx.coroutines)

    implementation(libs.bundles.networking)

    ksp(libs.bundles.networking.codegen)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.compose.runtime)

    debugImplementation(libs.bundles.flipper.debug)

    testImplementation(libs.bundles.unitTest)
    androidTestImplementation(libs.bundles.integrationTest)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}