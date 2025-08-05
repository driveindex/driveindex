package io.github.driveindex.drives.onedrive.core

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.regex.Pattern

data class AzureFailedResultDtoA(
    @param:JsonProperty("error")
    val error: String,
    @param:JsonProperty("errorDescription")
    val errorDescription: String,
    @param:JsonProperty("errorCodes")
    val errorCodes: List<Int>,
    @param:JsonProperty("timestamp")
    val timestamp: String,
    @param:JsonProperty("traceId")
    val traceId: String,
    @param:JsonProperty("correlationId")
    val correlationId: String,
)

data class AzureFailedResultDtoB(
    @param:JsonProperty("code")
    val code: String,
    @param:JsonProperty("message")
    val message: String,
)

data class AzurePortalDtoV1_Token(
    @param:JsonProperty("token_type")
    val tokenType: String,
    @param:JsonProperty("expires_in")
    private val expiresIn: Long,
    @param:JsonProperty("scope")
    val scope: String,
    @param:JsonProperty("access_token")
    val accessToken: String,
    @param:JsonProperty("refresh_token")
    val refreshToken: String,
) {
    val expires: Long get() = System.currentTimeMillis() + expiresIn * 1000
}

data class AzureGraphDtoV2_Me(
    @param:JsonProperty("displayName")
    val displayName: String,
    @param:JsonProperty("id")
    val id: String,
    @param:JsonProperty("userPrincipalName")
    val userPrincipalName: String,
)

data class AzureGraphDtoV2_Me_Drive_Root_Delta(
    @param:JsonProperty("@odata.nextLink")
    private val nextLink: String? = null,
    @param:JsonProperty("@odata.deltaLink")
    private val deltaLink: String? = null,
    @param:JsonProperty("value")
    val value: List<Value>
) {
    val nextToken: String get() = nextLink?.getToken() ?: ""
    val deltaToken: String? get() = deltaLink?.getToken()

    companion object {
        private val TokenPattern: Pattern = "token=(.*)".toPattern()
    }
    private fun String.getToken() = TokenPattern.matcher(this)
        .also { it.find() }.group().substring(6)

    data class Value(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("name")
        val name: String,
        @param:JsonProperty("size")
        val size: Long,
        @param:JsonProperty("webUrl")
        val webUrl: String? = null,
        @param:JsonProperty("parentReference")
        val parentReference: ParentReference? = null,
        @param:JsonProperty("folder")
        val folder: Unit? = null,
        @param:JsonProperty("file")
        val file: File? = null,
        @param:JsonProperty("deleted")
        val deleted: Unit? = null,
    ) {
        data class ParentReference(
            @param:JsonProperty("id")
            val id: String
        )
        data class File(
            @param:JsonProperty("mimeType")
            val mimeType: String,
            @param:JsonProperty("hashes")
            val hashes: Hashes? = null
        ) {
            data class Hashes(
                @param:JsonProperty("quickXorHash")
                val quickXorHash: String? = null,
                @param:JsonProperty("sha1Hash")
                val sha1Hash: String? = null,
                @param:JsonProperty("sha256Hash")
                val sha256Hash: String? = null,
            )
        }
    }
}