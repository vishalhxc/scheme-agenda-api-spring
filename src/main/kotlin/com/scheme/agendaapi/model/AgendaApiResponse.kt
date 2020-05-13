package com.scheme.agendaapi.model

data class AgendaApiResponse<T>(
        val status: String = "",
        val body: T
)