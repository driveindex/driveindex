package io.github.driveindex.drives.onedrive

import io.github.driveindex.annotations.DriveClient
import io.github.driveindex.drivers.api.BaseNetDiskDriver
import io.github.driveindex.drivers.api.ClientType
import io.github.driveindex.drives.onedrive.attr.OneDriveAccountAttribute
import io.github.driveindex.drives.onedrive.attr.OneDriveClientAttribute
import io.github.driveindex.drives.onedrive.core.OneDriveEndpoint
import io.github.driveindex.drives.onedrive.endpoints.EndpointRegistry
import kotlinx.serialization.modules.SerializersModuleBuilder

@DriveClient(ClientType.OneDrive)
class OneDrive(
    private val endpoints: EndpointRegistry,
): BaseNetDiskDriver() {
    override fun onCreated() {

    }

    override fun registerAttrJsonModule(module: SerializersModuleBuilder) {
        module.polymorphicClientAttr(OneDriveClientAttribute.serializer())
        module.polymorphicAccountAttr(OneDriveAccountAttribute.serializer())
    }
}
