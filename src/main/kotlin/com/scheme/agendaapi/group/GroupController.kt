package com.scheme.agendaapi.group

import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.model.AgendaApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
class GroupController(
        private val groupService: Group.Service
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGroup(@RequestBody group: GroupModel): AgendaApiResponse<GroupModel> {
        return AgendaApiResponse(
                httpStatus = HttpStatus.CREATED,
                body = groupService.createGroup(group))
    }

    @GetMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getGroupsForUser(@PathVariable("user_id") userId: String): AgendaApiResponse<List<GroupModel>> {
        return AgendaApiResponse(
                httpStatus = HttpStatus.OK,
                body = groupService.getGroupsForUser(userId)
        )
    }

    @DeleteMapping("/{group_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGroup(@PathVariable("group_id") groupId: String): AgendaApiResponse<Unit> {
        groupService.deleteGroup(groupId)
        return AgendaApiResponse(
                httpStatus = HttpStatus.NO_CONTENT
        )
    }

    @PatchMapping("/{group_id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateGroup(
            @PathVariable("group_id") groupId: String,
            @RequestBody group: GroupModel): AgendaApiResponse<GroupModel> {
        return AgendaApiResponse(
                httpStatus = HttpStatus.OK,
                body = groupService.updateGroup(groupId, group)
        )
    }
}