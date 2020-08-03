package com.scheme.agendaapi.group.service

import com.scheme.agendaapi.exception.BadRequestException
import com.scheme.agendaapi.exception.ConflictException
import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.Group
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.util.ErrorMessages
import org.springframework.stereotype.Component
import java.util.*

@Component
class GroupValidation(
        private val groupDao: Group.Dao
): Group.Service.Validation {
    override fun validateCreateGroup(group: GroupModel) {
        val errors = mutableListOf<String>()

        errors.addAll(validateUUID("userId", group.userId))
        errors.addAll(validateRequired("name", group.name))
        if (errors.isNotEmpty()) throw BadRequestException(ErrorMessages.VALIDATION_ERROR, null, errors)

        errors.addAll(validateGroupConflict(group.userId, group.name))
        if (errors.isNotEmpty()) throw ConflictException(ErrorMessages.VALIDATION_ERROR, null, errors)
    }

    override fun validateModifyGroup(groupId: String) {
        val errors = validateUUID("groupId", groupId)
        if (errors.isNotEmpty()) throw NotFoundException(ErrorMessages.NOT_FOUND)
    }

    override fun validateGetGroupsForUser(userId: String) {
        val errors = validateUUID("userId", userId)
        if (errors.isNotEmpty()) throw NotFoundException(ErrorMessages.NOT_FOUND)
    }

    private fun validateGroupConflict(userId: String, name: String): List<String> {
        if (groupDao.getGroupForUser(userId, name) != null)
            return listOf(Group.Constants.groupConflict(userId, name))
        return emptyList()
    }

    private fun validateUUID(field: String, value: String?): List<String> {
        validateRequired(field, value)
                .let { if (it.isNotEmpty()) return it }
        try {
            UUID.fromString(value)
        } catch (ex: IllegalArgumentException) {
            return listOf(ErrorMessages.invalidField(field, value!!))
        }
        return emptyList()
    }

    private fun validateRequired(field: String, value: String?): List<String> {
        if (value.isNullOrEmpty()) return listOf(ErrorMessages.requiredField(field))
        return emptyList()
    }
}