package com.scheme.agendaapi.exception

import org.springframework.http.HttpStatus


open class AgendaApiException(message: String?,
                              cause: Throwable?,
                              errors: List<String>,
                              status: HttpStatus) : Exception(message, cause) {
    val errors = errors
    val status = status
}

class BadRequestException(message: String?,
                          cause: Throwable?,
                          errors: List<String>
) : AgendaApiException(message, cause, errors, HttpStatus.BAD_REQUEST)

class UnauthorizedException(message: String?,
                            cause: Throwable?,
                            errors: List<String>) : AgendaApiException(message, cause, errors, HttpStatus.UNAUTHORIZED)

class NotFoundException(message: String?,
                        cause: Throwable?,
                        errors: List<String>) : AgendaApiException(message, cause, errors, HttpStatus.NOT_FOUND)

class InternalServerErrorException(message: String?,
                                   cause: Throwable?,
                                   errors: List<String>) : AgendaApiException(message, cause, errors, HttpStatus.INTERNAL_SERVER_ERROR)

class ServiceUnavailableException(message: String?,
                                  cause: Throwable?,
                                  errors: List<String>) : AgendaApiException(message, cause, errors, HttpStatus.SERVICE_UNAVAILABLE)
