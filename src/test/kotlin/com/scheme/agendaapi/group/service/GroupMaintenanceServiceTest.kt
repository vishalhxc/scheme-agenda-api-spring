package com.scheme.agendaapi.group.service

import com.nhaarman.mockitokotlin2.*
import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.data.GroupData
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.util.ErrorConstants
import com.scheme.agendaapi.util.MessageConstants
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupMaintenanceServiceTest {

    @Mock
    private lateinit var groupData: GroupData

    @InjectMocks
    private lateinit var groupMaintenanceService: GroupMaintenanceService

    private val uuid = UUID.fromString("f2ea0d36-7420-4de0-a748-5a26c7ef6487")


    @Test
    fun `Create Group, happy path - calls data class`() {
        val input = Group(name = "Don Draper", userId = uuid, color = "red")
        val expected = Group(id = uuid, name = "Don Draper", userId = uuid, color = "red")
        val mockGroup = Group(id = uuid, name = input.name, userId = input.userId, color = input.color)

        whenever(groupData.createGroup(input)).thenReturn(mockGroup)

        // act
        val actual = groupMaintenanceService.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(groupData).createGroup(input)
        verifyNoMoreInteractions(groupData)
    }

    @Test
    fun `Get Groups for User, happy path - Returns all groups for user`() {
        val input = uuid
        val expected = listOf(
                Group(id = uuid, name = "Joan Harris", userId = uuid, color = "pink"),
                Group(id = uuid, name = "Roger Sterling", userId = uuid, color = "white"))

        val mockGroups = listOf(
                Group(id = uuid, name = "Joan Harris", userId = uuid, color = "pink"),
                Group(id = uuid, name = "Roger Sterling", userId = uuid, color = "white"))

        whenever(groupData.getGroupsForUser(uuid)).thenReturn(mockGroups)

        // act
        val actual = groupMaintenanceService.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupData).getGroupsForUser(uuid)
        verifyNoMoreInteractions(groupData)
    }

    @Test
    fun `Get Groups for User, no results - throw not found`() {
        val input = uuid
        val expected = NotFoundException(MessageConstants.USER_ERROR,
                listOf(ErrorConstants.USER_HAS_NO_GROUPS))

        val mockGroups = listOf<Group>()

        whenever(groupData.getGroupsForUser(uuid)).thenReturn(mockGroups)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupMaintenanceService.getGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupData).getGroupsForUser(uuid)
        verifyNoMoreInteractions(groupData)
    }

    @Test
    fun `Delete Group, happy path - Deletes entity in database`() {
        val input = uuid

        doNothing().whenever(groupData).deleteGroup(input)

        // act
        groupMaintenanceService.deleteGroup(input)

        // assert
        verify(groupData).deleteGroup(uuid)
        verifyNoMoreInteractions(groupData)
    }
}