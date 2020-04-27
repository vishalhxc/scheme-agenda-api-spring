package com.scheme.agendaapi.exception.error

data class ErrorResponse(
        val status: String = "",
        val message: String = "",
        val errors: List<String> = emptyList()
)