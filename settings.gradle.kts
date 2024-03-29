pluginManagement  {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "HackerNewsV2"
include(":app")
include(":data")
include(":business")
include(":compose")
include(":benchmark")
include(":compose-tracer")
include(":baseline-profile")
include(":startup")
