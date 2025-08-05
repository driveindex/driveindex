package io.github.driveindex.dto.req.user

import com.fasterxml.jackson.annotation.JsonProperty

data class SetPwdReqDto(
    @param:JsonProperty("old_pwd")
    val oldPwd: String,
    @param:JsonProperty("new_pwd")
    val newPwd: String,
)