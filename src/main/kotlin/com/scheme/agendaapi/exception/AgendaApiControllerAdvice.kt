package com.scheme.agendaapi.exception

import com.fasterxml.jackson.core.JsonProcessingException
import com.scheme.agendaapi.exception.error.ErrorResponse
import com.scheme.agendaapi.util.ErrorMessageConstants
import com.scheme.agendaapi.util.wrapper.LoggingWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AgendaApiControllerAdvice(
        private val logger: LoggingWrapper
) {
    @ExceptionHandler(value = [AgendaApiException::class])
    fun handleAgendaApiException(exception: AgendaApiException): ResponseEntity<ErrorResponse> {
        logger.logError(ErrorMessageConstants.AGENDA_API_ERROR, exception)
        return ResponseEntity(
                ErrorResponse(
                        status = "${exception.status.value()}_${exception.status.name}",
                        message = exception.message ?: ErrorMessageConstants.GENERIC_ERROR,
                        errors = exception.errors),
                exception.status)
    }

    @ExceptionHandler(value = [JsonProcessingException::class])
    fun handleJsonException(exception: JsonProcessingException): ResponseEntity<ErrorResponse> {
        logger.logError(ErrorMessageConstants.AGENDA_API_ERROR, exception)
        return ResponseEntity(
                ErrorResponse(
                        status = "400_BAD_REQUEST",
                        message = ErrorMessageConstants.JSON_ERROR),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageException(exception: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        logger.logError(ErrorMessageConstants.AGENDA_API_ERROR, exception)
        return ResponseEntity(
                ErrorResponse(
                        status = "400_BAD_REQUEST",
                        message = ErrorMessageConstants.JSON_ERROR),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleUnexpectedExceptions(exception: Exception): ResponseEntity<ErrorResponse> {
        logger.logError(ErrorMessageConstants.UNEXPECTED_ERROR, exception)
        return ResponseEntity(
                ErrorResponse(
                        status = "500_INTERNAL_SERVER_ERROR",
                        message = ErrorMessageConstants.GENERIC_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR)
    }
}