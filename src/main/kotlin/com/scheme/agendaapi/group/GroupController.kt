package com.scheme.agendaapi.group

import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.group.service.GroupService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/groups")
class GroupController(
        private val groupService: GroupService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGroup(@RequestBody group: Group): Group {
        return groupService.createGroup(group)
    }

    @GetMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getGroupsForUser(@PathVariable("user_id") userId: String): List<Group> {
        return groupService.getGroupsForUser(
                UUID.fromString(userId))
    }

    @DeleteMapping("/{group_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGroup(@PathVariable("group_id") groupId: String) {
        return groupService.deleteGroup(
                UUID.fromString(groupId)
        )
    }
}