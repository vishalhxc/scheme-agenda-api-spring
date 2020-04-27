package com.scheme.agendaapi.group.data

import com.scheme.agendaapi.group.model.Group
import java.util.*

interface GroupData {
    fun createGroup(group: Group): Group
    fun getGroupsForUser(user: UUID): List<Group>
    fun deleteGroup(id: UUID)
}