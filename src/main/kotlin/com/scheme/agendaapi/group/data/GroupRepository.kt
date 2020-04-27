package com.scheme.agendaapi.group.data

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface GroupRepository: CrudRepository<GroupEntity, UUID> {
    fun findAllByUserId(userId: UUID): List<GroupEntity>
}