package com.scheme.agendaapi.group.model

import com.scheme.agendaapi.util.ValidationConstants
import java.util.*
import javax.validation.constraints.NotEmpty

data class Group(
        val id: String? = null,

        @field:NotEmpty(message = ValidationConstants.GROUP_NAME_REQUIRED)
        val name: String =  "",

        @field:NotEmpty(message = ValidationConstants.USER_ID_REQUIRED)
        val userId: String = "",

        val color: String? = null
)