package io.github.driveindex.dto.resp.account

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("onedrive")
data class OneDriveAccountDetail(
    val azureUserId: String,
): AccountDetail
