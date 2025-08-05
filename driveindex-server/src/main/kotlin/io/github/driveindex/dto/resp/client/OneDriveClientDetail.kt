package io.github.driveindex.dto.resp.client

import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.driveindex.core.client.onedrive.OneDriveEndpoint

@JsonTypeName("onedrive")
data class OneDriveClientDetail(
    val clientId: String,
    val tenantId: String,
    val endPoint: OneDriveEndpoint,
): ClientDetail
