package com.scheme.agendaapi.group.model

import com.scheme.agendaapi.util.ValidationConstants
import java.util.*
import javax.validation.constraints.NotEmpty

data class Group(
        val id: UUID? = null,

        @field:NotEmpty(message = ValidationConstants.GROUP_NAME_REQUIRED)
        val name: String = "",

        @field:NotEmpty(message = ValidationConstants.USER_ID_REQUIRED)
        val userId: UUID = UUID.fromString("22599e29-eb59-4872-9ca8-54545e9e9dd8"),

        val color: String? = null
)