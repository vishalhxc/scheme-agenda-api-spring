package com.scheme.agendaapi.exception

import com.fasterxml.jackson.core.JsonProcessingException
import com.scheme.agendaapi.exception.error.ErrorResponse
import com.scheme.agendaapi.util.ErrorMessages
import com.scheme.agendaapi.util.wrapper.LoggingWrapper
import com.scheme.agendaapi.util.wrapper.StringFormatWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AgendaApiControllerAdvice(
        private val logger: LoggingWrapper
) {
    @ExceptionHandler(value = [AgendaApiException::class])
    fun handleAgendaApiException(exception: AgendaApiException): ResponseEntity<ErrorResponse> {
        return getErrorResponse(
                exception = exception,
                status = exception.status,
                message = exception.message ?: ErrorMessages.AGENDA_API_ERROR,
                errors = exception.errors
        )
    }

    @ExceptionHandler(value = [JsonProcessingException::class, HttpMessageNotReadableException::class])
    fun handleJsonException(exception: JsonProcessingException): ResponseEntity<ErrorResponse> {
        return getErrorResponse(
                exception = exception,
                status = HttpStatus.BAD_REQUEST,
                message = ErrorMessages.JSON_ERROR
        )
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun handleMethodNotAllowed(exception: Exception): ResponseEntity<ErrorResponse> {
        return getErrorResponse(
                exception = exception,
                status = HttpStatus.METHOD_NOT_ALLOWED,
                message = ErrorMessages.METHOD_NOT_ALLOWED
        )
    }

    @ExceptionHandler(value = [HttpMediaTypeNotSupportedException::class])
    fun handleUnsupportedMediaType(exception: Exception): ResponseEntity<ErrorResponse> {
        return getErrorResponse(
                exception = exception,
                status = HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                message = ErrorMessages.MEDIA_TYPE_NOT_SUPPORTED
        )
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleUnexpectedExceptions(exception: Exception): ResponseEntity<ErrorResponse> {
        return getErrorResponse(
                exception = exception,
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                message = ErrorMessages.AGENDA_API_ERROR
        )
    }

    private fun getErrorResponse(exception: Exception,
                                 status: HttpStatus,
                                 message: String,
                                 errors: List<String> = emptyList()): ResponseEntity<ErrorResponse> {
        logger.logError(message, exception)
        return ResponseEntity(
                ErrorResponse(
                        status = StringFormatWrapper.formatHttpStatus(status),
                        message = message,
                        errors = if (errors.isEmpty()) null else errors),
                status)
    }
}