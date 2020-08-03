package com.scheme.agendaapi.exception

import org.springframework.http.HttpStatus


open class AgendaApiException(
        message: String?,
        cause: Throwable? = null,
        val errors: List<String>,
        val status: HttpStatus) : Exception(message, cause) {
}

class BadRequestException(
        message: String,
        cause: Throwable? = null,
        errors: List<String> = emptyList()
) : AgendaApiException(message, cause, errors, HttpStatus.BAD_REQUEST)

class UnauthorizedException(
        message: String,
        cause: Throwable? = null,
        errors: List<String> = emptyList()
) : AgendaApiException(message, cause, errors, HttpStatus.UNAUTHORIZED)

class NotFoundException(
        message: String,
        cause: Throwable? = null,
        errors: List<String> = emptyList()
) : AgendaApiException(message, cause, errors, HttpStatus.NOT_FOUND)

class ConflictException(
        message: String,
        cause: Throwable? = null,
        errors: List<String> = emptyList()
) : AgendaApiException(message, cause, errors, HttpStatus.CONFLICT)
