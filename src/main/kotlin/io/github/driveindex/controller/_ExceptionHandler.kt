package io.github.driveindex.controller

import io.github.driveindex.core.util.log
import io.github.driveindex.dto.resp.RespResult
import io.github.driveindex.exception.FailedResult
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
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
    fun handleFailedResult(e: FailedResult): RespResult<Nothing> {
        log.debug("响应错误信息", e)
        return e.resp()
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(e: NotImplementedError): RespResult<Nothing> {
        return FailedResult.NotImplementationError.resp()
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): RespResult<Nothing> {
        return FailedResult.NotFound.resp()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): RespResult<Nothing> {
        log.warn("未处理的错误", e)
        return FailedResult.InternalServerError.resp()
    }
}