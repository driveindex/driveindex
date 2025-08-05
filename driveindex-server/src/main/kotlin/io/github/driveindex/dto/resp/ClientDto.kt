package io.github.driveindex.dto.resp

import io.github.driveindex.core.client.ClientType
import io.github.driveindex.database.entity.IdEntity
import io.github.driveindex.dto.resp.client.ClientDetail

data class ClientDto(
    override val id: Int,
    var name: String,
    val type: ClientType,
    val createAt: Long,
    val modifyAt: Long?,
    val detail: ClientDetail,
): IdEntity<Int>
