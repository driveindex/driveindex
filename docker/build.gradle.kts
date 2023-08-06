import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import io.github.driveindex.findStringProperty
import io.github.driveindex.parentFile
import io.github.driveindex.requireStringProperty

plugins {
    id("com.bmuschko.docker-remote-api")
}

val registryHost = findStringProperty("publishing.gitlab.registry.host")

docker {
    // https://docs.gradle.org/current/userguide/kotlin_dsl.html#groovy_closures_from_kotlin
    registryCredentials {
        if (registryHost != null) {
            url = "https://$registryHost/v2/"
        }
        username = "mhmzx"
        password = requireStringProperty("publishing.gitlab.token")
        email = "sgpublic2002@gmail.com"
    }
}

val output = File(buildDir, "libs/${project.name}-$version.jar")
tasks.register<Copy>("syncWebAppArchive") {
    group = "deploy"
    dependsOn(":server:assemble", ":web:runBuild")
    from(output, findProject(":web")!!.file("src/dist"))
    into(tasks.getByName<Dockerfile>("createDockerfile").destFile.parentFile)
}

tasks.register<Dockerfile>("createDockerfile") {
    group = "deploy"
    dependsOn("syncWebAppArchive")
    destFile = File(buildDir, "docker/Dockerfile")
    from("nginx:alpine")
    runCommand("apk add openjdk17")
    copyFile(output.name, "/app/driveindex.jar")
    copyFile("dist", "/var/www/driveindex")
    copyFile(
        project.file("res/driveindex.nginx.conf").canonicalPath,
        "/etc/nginx/conf.d/driveindex.conf"
    )
    entryPoint("java")
    defaultCommand("-jar", "/app/driveindex.jar", "--config=/app/driveindex.ini")
    exposePort(8080)
    instruction("HEALTHCHECK CMD curl -f http://localhost:8080/api/health || exit 1")
}

val mImages = if (registryHost != null) {
    setOf(
        "$registryHost/drive-index/driveindex-server-boot:latest",
        "$registryHost/drive-index/driveindex-server-boot:$version",
    )
} else {
    setOf(
        "mhmzx/driveindex:latest",
        "mhmzx/driveindex:$version",
    )
}

tasks.register<DockerBuildImage>("buildImage") {
    group = "deploy"
    dependsOn("createDockerfile")
    pull = true
    inputDir = tasks.getByName<Dockerfile>("createDockerfile").destFile.parentFile
    images = mImages
}

tasks.register<DockerPushImage>("pushImage") {
    group = "deploy"
    dependsOn("buildImage")
    images = mImages
}
