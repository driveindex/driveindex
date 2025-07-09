package io.github.driveindex.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(
    val roleName: String,
    val authorities: Collection<GrantedAuthority>,
) {
    USER(SecurityConfig.ROLE_ADMIN, listOf(
        SimpleGrantedAuthority(SecurityConfig.ROLE_USER),
    )),
    ADMIN(SecurityConfig.ROLE_ADMIN, listOf(
        SimpleGrantedAuthority(SecurityConfig.ROLE_ADMIN),
        SimpleGrantedAuthority(SecurityConfig.ROLE_USER),
    )),
    ;
}

enum class UserPermission {

}