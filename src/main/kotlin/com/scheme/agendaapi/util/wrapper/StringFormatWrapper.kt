package com.scheme.agendaapi.util.wrapper

import org.springframework.http.HttpStatus

class StringFormatWrapper {
    companion object Formatter {
        fun formatHttpStatus(status: HttpStatus): String {
            return "${status.value()} ${status.name.toLowerCase().capitalize().replace("_", " ")}"
        }
    }
}
