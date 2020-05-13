package com.scheme.agendaapi.exception.error

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
        val status: String = "",
        val message: String = "",
        val errors: List<String>? = null
)