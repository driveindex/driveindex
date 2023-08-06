package io.github.driveindex

import org.gradle.api.Project

/**
 * @author sgpublic
 * @Date 2023/8/6 16:09
 */


fun Project.findStringProperty(name: String): String? {
    return System.getenv(name.replace(".", "_"))
        ?.takeIf { it.isNotBlank() }
        ?: findProperty(name)?.toString()
}
fun Project.requireStringProperty(name: String): String {
    return findStringProperty(name)
        ?: throw IllegalArgumentException("property not found")
}
