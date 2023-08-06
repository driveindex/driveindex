package io.github.driveindex

import org.gradle.api.file.RegularFileProperty
import java.io.File

/**
 * @author sgpublic
 * @Date 2023/8/6 16:31
 */

val RegularFileProperty.parentFile: File
    get() {
    return asFile.get().parentFile
}