package com.scheme.agendaapi.util.wrapper

import com.scheme.agendaapi.exception.BadRequestException
import com.scheme.agendaapi.util.ErrorMessageConstants
import org.springframework.stereotype.Component
import javax.validation.Validation

@Component
class ValidationWrapper {
    fun validate(objectToValidate: Any?) {
        with (Validation.buildDefaultValidatorFactory().validator
                .validate(objectToValidate)
                .map { "${it.propertyPath.toString().capitalize()}: ${it.message}" }) {
            ifEmpty { return }
            throw BadRequestException(ErrorMessageConstants.USER_ERROR, null, this)
        }
    }
}