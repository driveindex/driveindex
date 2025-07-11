package io.github.driveindex.module

import io.github.driveindex.security.DriveIndexUserDetails
import io.github.driveindex.security.DriveIndexUserDetailsManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class Current {
    val User: DriveIndexUserDetails
        get() = SecurityContextHolder.getContext().authentication.details as DriveIndexUserDetails
}

@Component
class MutableCurrent(
    private val userManager: DriveIndexUserDetailsManager,
): Current() {
    override var User: DriveIndexUserDetails
        get() = super.User
        set(value) {
            userManager.updateCustomUser(value)
        }
}