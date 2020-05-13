package com.scheme.agendaapi.group.service.maintenance

import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.data.GroupData
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.group.service.GroupService
import com.scheme.agendaapi.util.ErrorConstants
import com.scheme.agendaapi.util.ErrorMessageConstants
import org.springframework.stereotype.Service

@Service
class GroupMaintenance(
        private val groupData: GroupData
) : GroupService {

    override fun createGroup(group: Group): Group {
        return groupData.createGroup(group)
    }

    override fun getGroupsForUser(userId: String): List<Group> {
        return groupData.getGroupsForUser(userId)
                .ifEmpty { throw NotFoundException(ErrorMessageConstants.USER_ERROR, null,
                        listOf(ErrorConstants.USER_HAS_NO_GROUPS)) }
    }

    override fun deleteGroup(groupId: String) {
        groupData.deleteGroup(groupId)
    }
}