package com.scheme.agendaapi.exception

import org.springframework.http.HttpStatus


open class AgendaApiException(message: String?,
                              errors: List<String>,
                              status: HttpStatus) : Exception(message) {
    val errors = errors
    val status = status
}

class BadRequestException(message: String?,
                          errors: List<String>) : AgendaApiException(message, errors, HttpStatus.BAD_REQUEST)

class UnauthorizedException(message: String?,
                            errors: List<String>) : AgendaApiException(message, errors, HttpStatus.UNAUTHORIZED)

class NotFoundException(message: String?,
                        errors: List<String>) : AgendaApiException(message, errors, HttpStatus.NOT_FOUND)

class InternalServerErrorException(message: String?,
                        errors: List<String>) : AgendaApiException(message, errors, HttpStatus.INTERNAL_SERVER_ERROR)

class ServiceUnavailableException(message: String?,
                        errors: List<String>) : AgendaApiException(message, errors, HttpStatus.SERVICE_UNAVAILABLE)
