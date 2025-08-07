package io.github.driveindex.drives.onedrive

import feign.Request;
import io.github.driveindex.drivers.feign.DriverRemoteApiException

/**
 * @author sgpublic
 * @Date 2022/8/15 11:24
 */

class AzureDecodeException(
    status: Int,
    val code: String,
    override val message: String,
    request: Request,
) : DriverRemoteApiException(status, message, request)