package com.scheme.agendaapi.group

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.group.service.GroupService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupControllerTest {

    @Mock
    private lateinit var groupService: GroupService

    private lateinit var groupController: GroupController

    private val baseUrl ="http://scheme"

    val uuidString = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"
    val uuid = UUID.fromString(uuidString)

    @BeforeEach
    fun setUp() {
        groupController = GroupController(groupService)
    }

    @Test
    fun `Create Group, happy path - returns created group`() {
        val input = Group(
                name = "Walter White",
                userId = uuid,
                color = "tan")
        val expected = Group(
                id = uuid,
                name = "Walter White",
                userId = uuid,
                color = "tan")

        whenever(groupService.createGroup(input)).thenReturn(expected)

        // act
        val actual = groupController.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(groupService).createGroup(input)
        verifyNoMoreInteractions(groupService)
    }

    @Test
    fun `Get Groups for User, happy path - returns groups`() {
        val input = uuidString
        val expected = listOf(
                Group(id = uuid, name = "Hank Schrader", userId = uuid, color = "black"),
                Group(id = uuid, name = "Steven Gomez", userId = uuid, color = "yellow"))

        whenever(groupService.getGroupsForUser(uuid)).thenReturn(expected)

        // act
        val actual = groupController.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupService).getGroupsForUser(uuid)
        verifyNoMoreInteractions(groupService)
    }

    @Test
    fun `Delete Group, happy path - deletes`() {
        val input = uuidString

        doNothing().whenever(groupService).deleteGroup(uuid)

        // act
        val actual = groupController.deleteGroup(input)

        // assert
        verify(groupService).deleteGroup(uuid)
        verifyNoMoreInteractions(groupService)
    }
}