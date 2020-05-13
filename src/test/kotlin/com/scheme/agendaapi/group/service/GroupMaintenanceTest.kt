package com.scheme.agendaapi.group.service

import com.nhaarman.mockitokotlin2.*
import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.data.GroupData
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.group.service.maintenance.GroupMaintenance
import com.scheme.agendaapi.util.ErrorConstants
import com.scheme.agendaapi.util.ErrorMessageConstants
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupMaintenanceTest {

    @Mock
    private lateinit var groupData: GroupData

    @InjectMocks
    private lateinit var groupMaintenance: GroupMaintenance

    private val uuidString = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"
    private val uuid = UUID.fromString(uuidString)

    @Test
    fun `Create Group, happy path - calls data class`() {
        val input = Group(name = "Don Draper", userId = uuidString, color = "red")
        val expected = Group(id = uuidString, name = "Don Draper", userId = uuidString, color = "red")
        val mockGroup = Group(id = uuidString, name = "Don Draper", userId = uuidString, color = "red")

        whenever(groupData.createGroup(input)).thenReturn(mockGroup)

        // act
        val actual = groupMaintenance.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(groupData).createGroup(input)
        verifyNoMoreInteractions(groupData)
    }

    @Test
    fun `Get Groups for User, happy path - Returns all groups for user`() {
        val input = uuidString
        val expected = listOf(
                Group(id = uuidString, name = "Joan Harris", userId = uuidString, color = "pink"),
                Group(id = uuidString, name = "Roger Sterling", userId = uuidString, color = "white"))

        val mockGroups = listOf(
                Group(id = uuidString, name = "Joan Harris", userId = uuidString, color = "pink"),
                Group(id = uuidString, name = "Roger Sterling", userId = uuidString, color = "white"))

        whenever(groupData.getGroupsForUser(uuidString)).thenReturn(mockGroups)

        // act
        val actual = groupMaintenance.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupData).getGroupsForUser(uuidString)
        verifyNoMoreInteractions(groupData)
    }

    @Test
    fun `Get Groups for User, no results - throw not found`() {
        val input = uuidString
        val expected = NotFoundException(ErrorMessageConstants.USER_ERROR, null,
                listOf(ErrorConstants.USER_HAS_NO_GROUPS))

        val mockGroups = listOf<Group>()

        whenever(groupData.getGroupsForUser(uuidString)).thenReturn(mockGroups)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupMaintenance.getGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupData).getGroupsForUser(uuidString)
        verifyNoMoreInteractions(groupData)
    }

    @Test
    fun `Delete Group, happy path - Deletes entity in database`() {
        val input = uuidString

        doNothing().whenever(groupData).deleteGroup(uuidString)

        // act
        groupMaintenance.deleteGroup(input)

        // assert
        verify(groupData).deleteGroup(uuidString)
        verifyNoMoreInteractions(groupData)
    }
}