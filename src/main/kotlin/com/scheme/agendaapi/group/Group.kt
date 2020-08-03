package com.scheme.agendaapi.group

import com.scheme.agendaapi.group.model.GroupModel


interface Group {
    interface Service {
        fun createGroup(group: GroupModel): GroupModel
        fun getGroupsForUser(userId: String): List<GroupModel>
        fun deleteGroup(groupId: String)
        fun updateGroup(groupId: String, group: GroupModel): GroupModel

        interface Validation {
            fun validateCreateGroup(group: GroupModel)
            fun validateModifyGroup(groupId: String)
            fun validateGetGroupsForUser(userId: String)
        }
    }
    interface Dao {
        fun createGroup(group: GroupModel): GroupModel
        fun getGroupForUser(userId: String, name: String): GroupModel?
        fun getGroupsForUser(userId: String): List<GroupModel>
        fun deleteGroup(groupId: String): GroupModel?
        fun updateGroup(groupId: String, group: GroupModel): GroupModel?
    }
    interface Model {
        val id: String?
        val name: String
        val userId: String
        val color: String?
    }
    object Constants {
        fun groupConflict(userId: String, name: String):String {
            return "User ID '$userId' already has a group with the Name '$name'"
        }
    }
}