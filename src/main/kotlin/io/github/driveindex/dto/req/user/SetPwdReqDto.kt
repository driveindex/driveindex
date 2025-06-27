package io.github.driveindex.dto.req.user

import com.fasterxml.jackson.annotation.JsonProperty

data class SetPwdReqDto(
    @JsonProperty("old_pwd")
    val oldPwd: String,
    @JsonProperty("new_pwd")
    val newPwd: String,
)