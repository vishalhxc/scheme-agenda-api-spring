package com.scheme.agendaapi.group.model

import com.scheme.agendaapi.group.Group

data class GroupModel(
        override val id: String? = null,
        override val name: String =  "",
        override val userId: String = "",
        override val color: String? = null
) : Group.Model