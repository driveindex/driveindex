package io.github.driveindex.database.dao.attributes

import io.github.driveindex.database.entity.ClientEntity
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.drivers.attributes.AccountAttribute

sealed interface AttributeDao<AttT>

sealed interface AccountAttributeDao: AttributeDao<AccountAttribute>

sealed interface ClientAttributeDao: AttributeDao<ClientEntity>

sealed interface FileAttributeDao: AttributeDao<FileEntity>
