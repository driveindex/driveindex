package io.github.driveindex.database.entity.file.attributes

import io.github.driveindex.drivers.api.ClientType
import io.github.driveindex.dto.resp.file.OneDriveFileDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("onedrive")
data class OneDriveFileAttribute(
    @SerialName("type")
    override val type: ClientType,

    @SerialName("account_id")
    override val accountId: Int,

    @SerialName("azure_file_id")
    val fileId: String,

    @SerialName("web_url")
    val webUrl: String,

    @SerialName("mime_type")
    val mimeType: String,

    @SerialName("quick_xor_hash")
    val quickXorHash: String? = null,

    @SerialName("sha1_hash")
    val sha1Hash: String? = null,

    @SerialName("sha256_hash")
    val sha256Hash: String? = null,
): RemoteFileAttribute {
    override fun toRespDtoDetail() = OneDriveFileDetail(
        webUrl = webUrl,
        mimeType = mimeType,
        quickXorHash = quickXorHash,
        sha1Hash = sha1Hash,
        sha256Hash = sha256Hash,
    )
}