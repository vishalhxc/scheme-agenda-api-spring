package com.scheme.agendaapi.group.data

import com.scheme.agendaapi.group.Group
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.util.wrapper.UUIDWrapper
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class GroupDao(
        private val groupRepository: GroupRepository,
        private val uuidWrapper: UUIDWrapper
) : Group.Dao {
    override fun createGroup(group: GroupModel): GroupModel {
        return groupRepository.save(
                constructEntity(group))
                .toModel()
    }

    override fun getGroupForUser(userId: String, name: String): GroupModel? {
        return groupRepository.findByUserIdAndName(
                userId = userId.toUUID() ?: return null,
                name = name)
                ?.toModel() ?: return null
    }

    override fun getGroupsForUser(userId: String): List<GroupModel> {
        return groupRepository.findAllByUserId(userId.toUUID() ?: return emptyList())
                .map { it.toModel() }
    }

    override fun deleteGroup(groupId: String): GroupModel? {
        val group = getGroup(groupId)
        groupRepository.delete(group ?: return null)
        return group.toModel()
    }

    override fun updateGroup(groupId: String, group: GroupModel): GroupModel? {
        val originalEntity = getGroup(groupId) ?: return null
        return groupRepository.save(GroupEntity(
                id = originalEntity.id,
                userId = originalEntity.userId,
                name = if (group.name.isEmpty()) originalEntity.name else group.name,
                color = if (group.color.isNullOrEmpty()) originalEntity.color else group.color))
                .toModel()
    }

    private fun getGroup(groupId: String): GroupEntity? {
        return groupRepository
                .findById(groupId.toUUID()
                        ?: return null)
                .orElse(null)
    }

    private fun constructEntity(group: GroupModel): GroupEntity {
        return GroupEntity(
                userId = group.userId.toUUID()!!,
                id = uuidWrapper.generateUUID(),
                name = group.name,
                color = group.color
        )
    }

    private fun GroupEntity.toModel(): GroupModel {
        return GroupModel(
                id = this.id.toString(),
                name = this.name,
                userId = this.userId.toString(),
                color = this.color
        )
    }

    private fun String.toUUID(): UUID? {
        return try {
            UUID.fromString(this)
        } catch (ex: IllegalArgumentException) {
            null
        }
    }
}