package com.scheme.agendaapi.group

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.model.AgendaApiResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GroupControllerTest {
    @Mock private lateinit var groupService: Group.Service
    @InjectMocks private lateinit var groupController: GroupController

    @Test
    fun `Create Group, happy path - returns created group`() {
        val input = GroupModel(
                name = "Juarez Cartel",
                userId = "hectorSalamanca",
                color = "tan")
        val expected = AgendaApiResponse(
                status = "201 Created",
                body = GroupModel(
                        id = "id",
                        name = "Juarez Cartel",
                        userId = "hectorSalamanca",
                        color = "tan"))

        whenever(groupService.createGroup(input)).thenReturn(expected.body)

        // act
        val actual = groupController.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(groupService).createGroup(input)
        verifyNoMoreInteractions(groupService)
    }

    @Test
    fun `Get Groups for User, happy path - returns groups`() {
        val input = "hankschrader"
        val expected = AgendaApiResponse(
                status = "200 Ok",
                body = listOf(
                        GroupModel(id = "id", name = "DEA", userId = "hankschrader", color = "black"),
                        GroupModel(id = "id", name = "Schrader Family", userId = "hankschrader", color = "yellow")))

        whenever(groupService.getGroupsForUser("hankschrader")).thenReturn(expected.body)

        // act
        val actual = groupController.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupService).getGroupsForUser("hankschrader")
        verifyNoMoreInteractions(groupService)
    }

    @Test
    fun `Delete Group, happy path - deletes`() {
        val input = "Breaking Bad"

        doNothing().whenever(groupService).deleteGroup(input)

        // act
        groupController.deleteGroup(input)

        // assert
        verify(groupService).deleteGroup(input)
        verifyNoMoreInteractions(groupService)
    }

    @Test
    fun `Update Group, happy path - updates`() {
        val input = GroupModel(
                name = "Los Pollos Hermanos",
                userId = "gusfring",
                color = "green")
        val inputGroupId = ""
        val expected = AgendaApiResponse(
                status = "200 Ok",
                body = GroupModel(
                        id = "id",
                        name = "Los Pollos Hermanos",
                        userId = "gusfring",
                        color = "green"))

        whenever(groupService.updateGroup(inputGroupId, input)).thenReturn(expected.body)

        // act
        val actual = groupController.updateGroup(inputGroupId, input)

        // assert
        assertEquals(expected, actual)
        verify(groupService).updateGroup(inputGroupId, input)
        verifyNoMoreInteractions(groupService)
    }
}