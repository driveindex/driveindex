package io.github.driveindex.dto.resp.file

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("onedrive")
data class OneDriveFileDetail(
    val webUrl: String,
    val mimeType: String,
    val quickXorHash: String? = null,
    val sha1Hash: String? = null,
    val sha256Hash: String? = null,
): FileDetail