import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.kotlin.plugin.serialization)

    alias(libs.plugins.org.springframework.boot)
    // see https://youtrack.jetbrains.com/issue/KT-58021/NoClassDefFoundError-kotlin-enums-EnumEntries-with-language-version-1.9-or-2.0#focus=Comments-27-7955046.0-0
//    alias(libs.plugins.io.spring.dependency.management)

    application
}

group = "io.github.driveindex"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        jvmToolchain(17)
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
        withJava()
    }
    js {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
                devServer = KotlinWebpackConfig.DevServer(
                    static = mutableListOf(project.file("src/jsMain/resources").toString()),
                    // proxy api calls to springboot running on 3000 configured in application.yml
                    proxy = hashMapOf("/api" to "http://localhost:11511")
                )
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.kotlin.reflect)
                implementation(libs.kotlin.stdlib.jdk8)

                implementation(platform(SpringBootPlugin.BOM_COORDINATES))

                implementation(libs.spring.boot.starter)
                implementation(libs.spring.boot.starter.web)
                implementation(libs.spring.boot.starter.security)
//                implementation(libs.spring.boot.starter.websocket)

                implementation(libs.spring.cloud.starter.openfeign)

                implementation(libs.knife4j.openapi3.jakarta.spring.boot.starter)
                implementation(libs.springdoc.openapi.security)

                implementation(libs.spring.boot.starter.data.jpa)
                implementation(libs.spring.boot.starter.validation)
                runtimeOnly(libs.mariadb.java.client)

                runtimeOnly(libs.flyway.core)
                runtimeOnly(libs.flyway.mysql)
                runtimeOnly(libs.flyway.sqlserver)

                implementation(libs.ini4j)
                implementation(libs.kotlinx.serialization.json.jvm)

                implementation(libs.jjwt.api)
                runtimeOnly(libs.jjwt.impl)
                runtimeOnly(libs.jjwt.gson)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.spring.boot.test)
                implementation(libs.spring.boot.starter.test)
                implementation(libs.spring.security.test)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(platform(libs.kotlinw.wrappers.bom.get()))
                implementation(libs.kotlinw.node)
                implementation(libs.kotlinw.typescript)
                implementation(libs.kotlinw.emotion)
                implementation(libs.kotlinw.react)
                implementation(libs.kotlinw.react.dom)
                implementation(libs.kotlinw.react.router.dom)

                implementation(libs.kotlin.stdlib.js)
                implementation(libs.kotlinx.serialization.json.js)

                implementation(project(":hiui-kotlin"))
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("io.github.driveindex.Application")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}