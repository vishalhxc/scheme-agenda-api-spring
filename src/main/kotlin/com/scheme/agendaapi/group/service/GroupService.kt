package com.scheme.agendaapi.group.service

import com.scheme.agendaapi.group.model.Group
import java.util.*

interface GroupService {
    fun createGroup(group: Group): Group
    fun getGroupsForUser(user: UUID): List<Group>
    fun deleteGroup(id: UUID)
}