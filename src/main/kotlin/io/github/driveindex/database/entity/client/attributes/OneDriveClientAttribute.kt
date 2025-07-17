package io.github.driveindex.database.entity.client.attributes

import io.github.driveindex.core.client.onedrive.OneDriveEndpoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("onedrive")
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
