package io.github.driveindex.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.UserDetailsManager

@Suppress("UNCHECKED_CAST")
interface AbstractUserDetailsManager<T: UserDetails>: UserDetailsManager {
    override fun createUser(user: UserDetails) = createCustomUser(user as T)
    fun createCustomUser(user: T)

    override fun updateUser(user: UserDetails) = updateCustomUser(user as T)
    fun updateCustomUser(user: T)
}