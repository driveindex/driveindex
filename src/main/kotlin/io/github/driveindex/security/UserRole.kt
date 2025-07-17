package io.github.driveindex.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(
    val roleName: String,
    val authorities: Collection<GrantedAuthority>,
) {
    GUEST(SecurityConfig.ROLE_GUEST, listOf(
        SimpleGrantedAuthority(SecurityConfig.ROLE_GUEST),
    )),
    USER(SecurityConfig.ROLE_USER, listOf(
        SimpleGrantedAuthority(SecurityConfig.ROLE_USER),
        SimpleGrantedAuthority(SecurityConfig.ROLE_GUEST),
    )),
    ADMIN(SecurityConfig.ROLE_ADMIN, listOf(
        SimpleGrantedAuthority(SecurityConfig.ROLE_ADMIN),
        SimpleGrantedAuthority(SecurityConfig.ROLE_USER),
        SimpleGrantedAuthority(SecurityConfig.ROLE_GUEST),
    )),
    ;
}

enum class UserPermission: GrantedAuthority {
    PERMISSION_CREATE_MOUNT,
    PERMISSION_MANAGE_LOCAL_FILE,
    PERMISSION_ACCESS_HIDDEN_FILE,
    PERMISSION_ACCESS_SHARED_WITHOUT_PASSWORD,
    ;

    override fun getAuthority(): String {
        return name
    }

    companion object {
        val GROUP_ADMIN = entries.toSet()
        val GROUP_USER = setOf(PERMISSION_CREATE_MOUNT, PERMISSION_MANAGE_LOCAL_FILE)
    }
}