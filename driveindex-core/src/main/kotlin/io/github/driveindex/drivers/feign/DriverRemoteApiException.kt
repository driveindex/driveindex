package io.github.driveindex.drivers.feign

import feign.Request
import feign.codec.DecodeException

open class DriverRemoteApiException(
    status: Int,
    override val message: String,
    request: Request,
): DecodeException(status, message, request)