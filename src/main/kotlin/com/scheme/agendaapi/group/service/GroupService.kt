package com.scheme.agendaapi.group.service

import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.Group
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.util.ErrorMessages
import org.springframework.stereotype.Service

@Service
internal class GroupService(
        private val groupDao: Group.Dao,
        private val groupServiceValidation: Group.Service.Validation
) : Group.Service {

    override fun createGroup(group: GroupModel): GroupModel {
        groupServiceValidation.validateCreateGroup(group)
        return groupDao.createGroup(group)
    }

    override fun getGroupsForUser(userId: String): List<GroupModel> {
        groupServiceValidation.validateGetGroupsForUser(userId)
        return groupDao.getGroupsForUser(userId)
                .ifEmpty { throw NotFoundException(ErrorMessages.NOT_FOUND) }
    }

    override fun deleteGroup(groupId: String) {
        groupServiceValidation.validateModifyGroup(groupId)
        groupDao.deleteGroup(groupId) ?: throw NotFoundException(ErrorMessages.NOT_FOUND)
    }

    override fun updateGroup(groupId: String, group: GroupModel): GroupModel {
        groupServiceValidation.validateModifyGroup(groupId)
        return groupDao.updateGroup(groupId, group) ?: throw NotFoundException(ErrorMessages.NOT_FOUND)
    }
}