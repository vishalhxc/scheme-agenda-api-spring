package com.scheme.agendaapi.group.service

import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.data.GroupData
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.util.ErrorConstants
import com.scheme.agendaapi.util.MessageConstants
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupMaintenanceService(
        private val groupData: GroupData
) : GroupService {

    override fun createGroup(group: Group): Group {
        return groupData.createGroup(group)
    }

    override fun getGroupsForUser(user: UUID): List<Group> {
        val groups = groupData.getGroupsForUser(user)
        if (groups.isEmpty()) {
            throw NotFoundException(MessageConstants.USER_ERROR,
                    listOf(ErrorConstants.USER_HAS_NO_GROUPS))
        }
        return groups
    }

    override fun deleteGroup(id: UUID) {
        groupData.deleteGroup(id)
    }
}