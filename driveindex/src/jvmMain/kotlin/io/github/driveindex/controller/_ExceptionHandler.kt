package io.github.driveindex.controller

import io.github.driveindex.core.util.log
import io.github.driveindex.exception.AzureDecodeException
import io.github.driveindex.exception.FailedRespResult
import io.github.driveindex.exception.FailedResult
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
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
    fun handleFailedResult(e: FailedResult): FailedRespResult {
        log.debug("响应错误信息", e)
        return e.resp()
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(AzureDecodeException::class)
    fun handleAzureDecodeException(
        exception: AzureDecodeException,
    ): FailedRespResult {
        log.warn("未捕获的 Azure 接口解析错误", exception)
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
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): FailedRespResult {
        log.trace("参数缺失", e)
        return FailedResult.MissingBody.resp()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): FailedRespResult {
        log.trace("参数缺失", e)
        return FailedResult.MissingBody(e.parameterName, e.parameterType).resp()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): FailedRespResult {
        log.warn("未处理的错误", e)
        return FailedResult.InternalServerError.resp()
    }
}