pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "driveindex-fullstack"

include("driveindex")
include("hiui-kotlin")
include("hiui-core-kotlin")
