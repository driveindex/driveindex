package io.github.driveindex.database.entity.client.attributes

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.driveindex.client.onedrive.OneDriveEndpoint

data class OneDriveClientAttribute(
    @JsonProperty("azure_client_id")
    val clientId: String,

    @JsonProperty("azure_client_secret")
    var clientSecret: String,

    @JsonProperty("azure_client_tenant")
    val tenantId: String = "common",

    @JsonProperty("azure_client_endpoint")
    val endPoint: OneDriveEndpoint = OneDriveEndpoint.Global,
): ClientAttribute
