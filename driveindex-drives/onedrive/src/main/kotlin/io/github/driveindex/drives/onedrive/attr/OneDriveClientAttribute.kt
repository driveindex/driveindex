package io.github.driveindex.drives.onedrive.attr

import io.github.driveindex.drives.attributes.ClientAttribute
import io.github.driveindex.drives.onedrive.OneDrive
import io.github.driveindex.drives.onedrive.core.OneDriveEndpoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(OneDrive.Name)
data class OneDriveClientAttribute(
    @SerialName("azure_client_id")
    val clientId: String,

    @SerialName("azure_client_secret")
    var clientSecret: String,

    @SerialName("azure_client_tenant")
    val tenantId: String = "common",

    @SerialName("azure_client_endpoint")
    val endPoint: OneDriveEndpoint = OneDriveEndpoint.Global,
): ClientAttribute
