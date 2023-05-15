import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.blp)
}

android {
    namespace = "com.schinizer.hackernews.profilegenerator"
    compileSdk = 33

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions {
        managedDevices {
            devices {
                create("pixel6Api31", ManagedVirtualDevice::class) {
                    device = "Pixel 6"
                    apiLevel = 31
                    systemImageSource = "aosp"
                }
            }
        }
    }
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on. The
    // default is none.
    managedDevices += "pixel6Api31"

    // This enables using connected devices to generate profiles. The default is
    // true. When using connected devices, they must be rooted or API 33 and
    // higher.
    useConnectedDevices = false
}

dependencies {
    implementation(libs.bundles.integrationTest)
}