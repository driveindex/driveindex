package io.github.driveindex.database.entity.file.attributes

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OneDriveFileAttribute(
    @JsonProperty("azure_account_id")
    val accountId: UUID,

    @JsonProperty("file_id")
    val fileId: String,

    @JsonProperty("web_url")
    val webUrl: String,

    @JsonProperty("mime_type")
    val mimeType: String,

    @JsonProperty("quick_xor_hash")
    val quickXorHash: String? = null,

    @JsonProperty("sha1_hash")
    val sha1Hash: String? = null,

    @JsonProperty("sha256_hash")
    val sha256Hash: String? = null,
): FileAttribute