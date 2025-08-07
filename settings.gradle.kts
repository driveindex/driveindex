pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}


rootProject.name = "DriveIndex"

include(":driveindex-core")
include(":driveindex-server")

include(":driveindex-drivers")
File(rootDir, "driveindex-drivers")
    .listFiles { file -> file.isDirectory && File(file, "build.gradle.kts").exists() }
    ?.forEach {
        include(":driveindex-drivers:${it.name}")
    }
