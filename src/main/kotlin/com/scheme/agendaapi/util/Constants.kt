package com.scheme.agendaapi.util

object ErrorMessages {
    const val VALIDATION_ERROR = "Validation error"
    const val AGENDA_API_ERROR = "Agenda API error"
    const val UNEXPECTED_ERROR = "Unexpected error"
    const val JSON_ERROR = "JSON processing error"
    const val NOT_FOUND = "Resource does not exist"
    const val METHOD_NOT_ALLOWED = "Http Method is not allowed"
    const val MEDIA_TYPE_NOT_SUPPORTED = "Media is not supported, use 'application/json'"

    fun invalidField(field: String, value: String): String {
        return "'$value' is not valid input for $field"
    }

    fun requiredField(field: String): String {
        return "$field is a required field"
    }
}