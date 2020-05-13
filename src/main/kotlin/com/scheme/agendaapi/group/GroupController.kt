package com.scheme.agendaapi.group

import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.group.service.GroupService
import com.scheme.agendaapi.model.AgendaApiResponse
import com.scheme.agendaapi.util.wrapper.ValidationWrapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
class GroupController(
        private val groupService: GroupService,
        private val validationWrapper: ValidationWrapper
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGroup(@RequestBody group: Group): AgendaApiResponse<Group> {
        validationWrapper.validate(group)
        return AgendaApiResponse(
                status = "201_CREATED",
                body = groupService.createGroup(group))
    }

    @GetMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getGroupsForUser(@PathVariable("user_id") userId: String): AgendaApiResponse<List<Group>> {
        return AgendaApiResponse(
                status = "200_OK",
                body = groupService.getGroupsForUser(userId)
        )
    }

    @DeleteMapping("/{group_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGroup(@PathVariable("group_id") groupId: String) {
        return groupService.deleteGroup(groupId)
    }
}