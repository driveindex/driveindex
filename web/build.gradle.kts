
import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.pnpm.task.PnpmTask
import io.github.driveindex.tasks.CheckoutTask

plugins {
    id("com.github.node-gradle.node")
}

node {
    download = true
    version = "18.16.0"
    nodeProjectDir = project.file("src")
}

tasks.register<PnpmTask>("runBuild") {
    dependsOn("pnpmInstall")
    args = listOf("run", "build")
}