package io.github.driveindex.dto.resp.client

import io.github.driveindex.client.onedrive.OneDriveEndpoint

data class OneDriveClientDetail(
    val clientId: String,
    val tenantId: String,
    val endPoint: OneDriveEndpoint,
): ClientDetail