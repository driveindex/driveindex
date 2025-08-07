package io.github.driveindex.drives.onedrive.endpoints

import io.github.driveindex.drives.onedrive.core.AzureErrorDecoder
import io.github.driveindex.drives.onedrive.core.OneDriveEndpoint
import io.github.driveindex.utils.RemoteHttpClientFactory
import org.springframework.stereotype.Component

@Component
class CNGlobal(
    errorDecoder: AzureErrorDecoder,
    feignFactory: RemoteHttpClientFactory,
): BaseEndpoint(OneDriveEndpoint.CN, errorDecoder, feignFactory)