package com.scheme.agendaapi.group

import com.scheme.agendaapi.group.model.Group
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
class GroupController(
        private val groupService: GroupService
) {
    @PostMapping
    fun createGroup(@RequestBody group: Group): Group {
        return Group()
    }
}