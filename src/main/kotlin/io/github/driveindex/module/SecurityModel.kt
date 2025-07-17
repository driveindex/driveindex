package io.github.driveindex.module

import io.github.driveindex.security.DriveIndexToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class Current {
    val User: DriveIndexToken?
        get() = SecurityContextHolder.getContext().authentication as DriveIndexToken?

    val AuthedUser: DriveIndexToken
        get() = User!!
}
