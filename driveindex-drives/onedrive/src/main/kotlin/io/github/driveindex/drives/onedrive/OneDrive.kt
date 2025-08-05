package io.github.driveindex.drives.onedrive

import com.google.auto.service.AutoService
import io.github.driveindex.drives.api.BaseDriveClient
import io.github.driveindex.drives.api.DriveClient
import io.github.driveindex.drives.onedrive.attr.OneDriveAccountAttribute
import io.github.driveindex.drives.onedrive.attr.OneDriveClientAttribute
import kotlinx.serialization.modules.SerializersModuleBuilder

@AutoService(DriveClient::class)
class OneDrive: BaseDriveClient() {
    override val name: String = Name
    override fun onCreated() {

    }

    override fun registerAttrJsonModule(module: SerializersModuleBuilder) {
        module.polymorphicClientAttr(OneDriveClientAttribute.serializer())
        module.polymorphicAccountAttr(OneDriveAccountAttribute.serializer())
    }

    companion object {
        const val Name: String = "OneDrive"
    }
}