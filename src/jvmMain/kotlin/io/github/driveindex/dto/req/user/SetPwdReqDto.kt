package io.github.driveindex.dto.req.user

import kotlinx.serialization.Serializable

@Serializable
data class SetPwdReqDto(
    val oldPwd: String,
    val newPwd: String,
)