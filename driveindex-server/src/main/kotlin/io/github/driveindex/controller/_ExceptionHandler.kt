package io.github.driveindex.controller

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.github.driveindex.utils.log
import io.github.driveindex.dto.resp.FailedRespResult
import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.drivers.feign.DriverRemoteApiException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException


/**
 * @author sgpublic
 * @Date 2023/2/8 9:19
 */
@Suppress("ClassName")
@RestControllerAdvice
class _ExceptionHandler {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(FailedResult::class)
    fun handleFailedResult(e: FailedResult): FailedRespResult {
        log.debug("Failed result response", e)
        return e.resp()
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(DriverRemoteApiException::class)
    fun handleDriverRemoteApiDecodeException(
        exception: DriverRemoteApiException,
    ): FailedRespResult {
        log.warn("Uncaught driver remote api error!", exception)
        return FailedResult.BadGateway(exception.message).resp()
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(e: NotImplementedError): FailedRespResult {
        return FailedResult.NotImplementationError.resp()
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): FailedRespResult {
        return FailedResult.NotFound.resp()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MismatchedInputException::class)
    fun handleMismatchedInputException(e: MismatchedInputException): FailedRespResult {
        log.trace("Args missing", e)
        return FailedResult.MissingBody.resp()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(e: Exception): FailedRespResult {
        log.warn("Uncaught error", e)
        return FailedResult.InternalServerError.resp()
    }
}