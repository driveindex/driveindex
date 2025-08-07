plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    projectDir.listFiles { file ->
        file.isDirectory && File(file, "build.gradle.kts").exists()
    }?.forEach {
        implementation(project(":driveindex-drivers:${it.name}"))
    }
}
