package io.github.driveindex.database.dao.attributes

import io.github.driveindex.database.entity.account.attributes.AccountAttribute
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.database.entity.file.FileEntity

sealed interface AttributeDao<AttT>

sealed interface AccountAttributeDao: AttributeDao<AccountAttribute>

sealed interface ClientAttributeDao: AttributeDao<ClientEntity>

sealed interface FileAttributeDao: AttributeDao<FileEntity>
