pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}


rootProject.name = "driveindex-deploy"

include(":server")
project(":server").run {
    projectDir = File(rootDir, "./server/server")
}
include(":web")
include(":docker")
