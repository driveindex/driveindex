package io.github.driveindex.dto.resp

import io.github.driveindex.database.entity.IdEntity
import io.github.driveindex.drivers.api.ClientType

data class ClientDto(
    override val id: Int,
    var name: String,
    val type: ClientType,
    val createAt: Long,
    val modifyAt: Long?,
): IdEntity<Int>
