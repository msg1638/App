pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
include(":unityLibrary")
project(":unityLibrary").projectDir= File(rootDir, "unityLibrary")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("${project(":unityLibrary").projectDir}/libs")
        }
    }
}

rootProject.name = "FCMtest"
include(":app")

