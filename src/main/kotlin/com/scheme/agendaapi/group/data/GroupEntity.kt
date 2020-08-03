package com.scheme.agendaapi.group.data

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "grouping")
internal data class GroupEntity(
        @Id
        @Column(name = "id")
        val id: UUID,

        @Column(name = "name", length = 25)
        val name: String,

        @Column(name = "user_id")
        val userId: UUID,

        @Column(name = "color", length = 25)
        val color: String?
)