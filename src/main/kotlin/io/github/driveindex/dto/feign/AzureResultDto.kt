package io.github.driveindex.dto.feign

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.regex.Pattern

data class AzureFailedResultDtoA(
    @JsonProperty("error")
    val error: String,
    @JsonProperty("errorDescription")
    val errorDescription: String,
    @JsonProperty("errorCodes")
    val errorCodes: List<Int>,
    @JsonProperty("timestamp")
    val timestamp: String,
    @JsonProperty("traceId")
    val traceId: String,
    @JsonProperty("correlationId")
    val correlationId: String,
)

data class AzureFailedResultDtoB(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("message")
    val message: String,
)

data class AzurePortalDtoV1_Token(
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("expires_in")
    private val expiresIn: Long,
    @JsonProperty("scope")
    val scope: String,
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
) {
    val expires: Long get() = System.currentTimeMillis() + expiresIn * 1000
}

data class AzureGraphDtoV2_Me(
    @JsonProperty("displayName")
    val displayName: String,
    @JsonProperty("id")
    val id: String,
    @JsonProperty("userPrincipalName")
    val userPrincipalName: String,
)

data class AzureGraphDtoV2_Me_Drive_Root_Delta(
    @JsonProperty("@odata.nextLink")
    private val nextLink: String? = null,
    @JsonProperty("@odata.deltaLink")
    private val deltaLink: String? = null,
    @JsonProperty("value")
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
        @JsonProperty("id")
        val id: String,
        @JsonProperty("name")
        val name: String,
        @JsonProperty("size")
        val size: Long,
        @JsonProperty("webUrl")
        val webUrl: String? = null,
        @JsonProperty("parentReference")
        val parentReference: ParentReference? = null,
        @JsonProperty("folder")
        val folder: Unit? = null,
        @JsonProperty("file")
        val file: File? = null,
        @JsonProperty("deleted")
        val deleted: Unit? = null,
    ) {
        data class ParentReference(
            @JsonProperty("id")
            val id: String
        )
        data class File(
            @JsonProperty("mimeType")
            val mimeType: String,
            @JsonProperty("hashes")
            val hashes: Hashes? = null
        ) {
            data class Hashes(
                @JsonProperty("quickXorHash")
                val quickXorHash: String? = null,
                @JsonProperty("sha1Hash")
                val sha1Hash: String? = null,
                @JsonProperty("sha256Hash")
                val sha256Hash: String? = null,
            )
        }
    }
}