package io.github.driveindex.dto.req.user

data class SetPwdReqDto(
    val oldPwd: String,
    val oldPwdRepeat: String,
    val newPwd: String,
)