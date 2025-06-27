package io.github.driveindex.exception

import feign.Request;
import feign.codec.DecodeException

/**
 * @author sgpublic
 * @Date 2022/8/15 11:24
 */

class AzureDecodeException(
    status: Int,
    val code: String,
    override val message: String,
    request: Request,
) : DecodeException(status, message, request)