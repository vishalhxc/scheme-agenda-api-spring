package com.scheme.agendaapi.group.data

import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.util.UUIDWrapper
import org.springframework.stereotype.Component
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

    override fun getGroupsForUser(user: UUID): List<Group> {
        return groupRepository.findAllByUserId(user)
                .map{ it.toGroup() }
    }

    override fun deleteGroup(id: UUID) {
        groupRepository.deleteById(id)
    }

    private fun constructEntity(group: Group): GroupEntity {
        return GroupEntity(
                id = uuidWrapper.generateUUID(),
                name = group.name,
                userId = group.userId,
                color = group.color
        )
    }

    private fun GroupEntity.toGroup(): Group {
        return Group(
                id = this.id,
                name = this.name,
                userId = this.userId,
                color = this.color
        )
    }
}