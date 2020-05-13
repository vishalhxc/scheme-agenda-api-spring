package com.scheme.agendaapi.group.data.dao

import com.scheme.agendaapi.exception.BadRequestException
import com.scheme.agendaapi.exception.InternalServerErrorException
import com.scheme.agendaapi.group.data.GroupData
import com.scheme.agendaapi.group.data.repository.GroupRepository
import com.scheme.agendaapi.group.data.entity.GroupEntity
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.util.ErrorConstants
import com.scheme.agendaapi.util.ErrorMessageConstants
import com.scheme.agendaapi.util.wrapper.UUIDWrapper
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.*

@Component
internal class GroupDao(
        private val groupRepository: GroupRepository,
        private val uuidWrapper: UUIDWrapper
): GroupData {
    override fun createGroup(group: Group): Group {
        return groupRepository.save(
                constructEntity(group))
                .toGroup()
    }

    override fun getGroupsForUser(userId: String): List<Group> {
        return groupRepository.findAllByUserId(userId.toUUID())
                .map{ it.toGroup() }
    }

    override fun deleteGroup(groupId: String) {
        groupRepository.deleteById(groupId.toUUID())
    }

    private fun constructEntity(group: Group): GroupEntity {
        return GroupEntity(
                userId = group.userId.toUUID(),
                id = uuidWrapper.generateUUID(),
                name = group.name,
                color = group.color
        )
    }

    private fun GroupEntity.toGroup(): Group {
        return Group(
                id = this.id.toString(),
                name = this.name,
                userId = this.userId.toString(),
                color = this.color
        )
    }

    private fun String.toUUID(): UUID {
        return try {
            UUID.fromString(this)
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException(ErrorMessageConstants.UUID_ERROR, ex,
                    listOf(ErrorConstants.INVALID_UUID))
        } catch (ex: RuntimeException) {
            throw InternalServerErrorException(ErrorMessageConstants.UNEXPECTED_ERROR, ex,
                    listOf(ErrorMessageConstants.UNEXPECTED_ERROR))
        }
    }
}