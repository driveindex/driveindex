package io.github.driveindex.controller

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.github.driveindex.core.util.log
import io.github.driveindex.dto.resp.SampleRespResult
import io.github.driveindex.core.exception.AzureDecodeException
import io.github.driveindex.core.exception.FailedResult
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
    fun handleFailedResult(e: FailedResult): SampleRespResult {
        log.debug("响应错误信息", e)
        return e.resp()
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(AzureDecodeException::class)
    fun handleAzureDecodeException(
        exception: AzureDecodeException,
    ): SampleRespResult {
        log.warn("未捕获的 Azure 接口解析错误", exception)
        return FailedResult.BadGateway(exception.message).resp()
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(e: NotImplementedError): SampleRespResult {
        return FailedResult.NotImplementationError.resp()
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): SampleRespResult {
        return FailedResult.NotFound.resp()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MismatchedInputException::class)
    fun handleMismatchedInputException(e: MismatchedInputException): SampleRespResult {
        log.trace("参数缺失", e)
        return FailedResult.MissingBody.resp()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): SampleRespResult {
        log.warn("未处理的错误", e)
        return FailedResult.InternalServerError.resp()
    }
}