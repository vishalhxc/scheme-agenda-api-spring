package com.scheme.agendaapi.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.scheme.agendaapi.util.wrapper.StringFormatWrapper
import org.springframework.http.HttpStatus

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class AgendaApiResponse<T>(
        val status: String = "",
        val body: T? = null
) {
    constructor(
            httpStatus: HttpStatus,
            body: T? = null
    ) : this(
            status = StringFormatWrapper.formatHttpStatus(httpStatus),
            body = body
    )
}