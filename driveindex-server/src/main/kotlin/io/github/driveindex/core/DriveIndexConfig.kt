package io.github.driveindex.core

import io.github.driveindex.Application
import io.github.driveindex.utils.MD5_FULL
import kotlinx.serialization.Serializable
import java.util.*

/**
 * @author sgpublic
 * @Date 2022/8/5 11:41
 */
@Serializable
data class DriveIndexConfig(
    val system: SystemConfig = SystemConfig(),
    val sql: SqlConfig = SqlConfig(),
    val app: AppConfig = AppConfig(),
    val token: TokenConfig = TokenConfig(),
) {
    @Serializable
    data class SystemConfig(
        val port: Int = 8080,
        val debug: Boolean = false,
        val trace: Boolean = false,
        val logDir: String = "/var/log/${Application.BASE_NAME_LOWER}",
    )
    @Serializable
    data class SqlConfig(
        val username: String = Application.BASE_NAME_LOWER,
        val password: String = "",
        val host: String = "localhost:3306",
        val database: String = Application.BASE_NAME_LOWER,
    )
    @Serializable
    data class AppConfig(
        val defaultUsername: String = "admin",
        val defaultPassword: String = UUID.randomUUID().toString().MD5_FULL,
        val allowRegister: Boolean = false,
    )
    @Serializable
    data class TokenConfig(
        val jwtSecurity: String = UUID.randomUUID().toString().MD5_FULL,
        val expired: Long = 3600 * 24,
    )
}
