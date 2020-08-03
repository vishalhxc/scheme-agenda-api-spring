package com.scheme.agendaapi.group.service

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.exception.BadRequestException
import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.Group
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.util.ErrorMessages
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GroupServiceTest {
    @Mock private lateinit var groupDao: Group.Dao
    @Mock private lateinit var groupServiceValidation: GroupValidation
    @InjectMocks private lateinit var groupService: GroupService

    private val uuidString = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"

    @Test
    fun `Create Group, happy path - calls data class`() {
        val input = GroupModel(name = "Don Draper", userId = uuidString, color = "red")
        val expected = GroupModel(id = uuidString, name = "Don Draper", userId = uuidString, color = "red")
        val mockGroup = GroupModel(id = uuidString, name = "Don Draper", userId = uuidString, color = "red")

        whenever(groupServiceValidation.validateCreateGroup(input)).then{/*do nothing*/}
        whenever(groupDao.createGroup(input)).thenReturn(mockGroup)

        // act
        val actual = groupService.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(groupServiceValidation).validateCreateGroup(input)
        verify(groupDao).createGroup(input)
        verifyNoMoreInteractions(groupServiceValidation, groupDao)
    }

    @Test
    fun `Create Group, validation fails - throw exception`() {
        val input = GroupModel(name = "", userId = "", color = "red")
        val expected = BadRequestException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(
                        ErrorMessages.requiredField("userId"),
                        ErrorMessages.requiredField("name")))

        doAnswer { throw expected }.whenever(groupServiceValidation).validateCreateGroup(input)

        // act
        val actual = assertThrows (expected.javaClass) {
            groupService.createGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupServiceValidation).validateCreateGroup(input)
        verifyNoMoreInteractions(groupServiceValidation)
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `Get Groups for User, happy path - Returns all groups for user`() {
        val input = uuidString
        val expected = listOf(
                GroupModel(id = uuidString, name = "Joan Harris", userId = uuidString, color = "pink"),
                GroupModel(id = uuidString, name = "Roger Sterling", userId = uuidString, color = "white"))

        val mockGroups = listOf(
                GroupModel(id = uuidString, name = "Joan Harris", userId = uuidString, color = "pink"),
                GroupModel(id = uuidString, name = "Roger Sterling", userId = uuidString, color = "white"))

        whenever(groupServiceValidation.validateGetGroupsForUser(input)).then{/*do nothing*/}
        whenever(groupDao.getGroupsForUser(uuidString)).thenReturn(mockGroups)

        // act
        val actual = groupService.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupServiceValidation).validateGetGroupsForUser(input)
        verify(groupDao).getGroupsForUser(uuidString)
        verifyNoMoreInteractions(groupServiceValidation, groupDao)
    }

    @Test
    fun `Get Groups for User, no results - throw not found`() {
        val input = uuidString
        val expected = NotFoundException(ErrorMessages.NOT_FOUND)

        val mockGroups = listOf<GroupModel>()

        whenever(groupServiceValidation.validateGetGroupsForUser(input)).then{/*do nothing*/}
        whenever(groupDao.getGroupsForUser(uuidString)).thenReturn(mockGroups)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupService.getGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupServiceValidation).validateGetGroupsForUser(input)
        verify(groupDao).getGroupsForUser(uuidString)
        verifyNoMoreInteractions(groupServiceValidation, groupDao)
    }

    @Test
    fun `Get Groups for User, validation fails - throw exception`() {
        val input = ""
        val expected = NotFoundException(
                message = ErrorMessages.NOT_FOUND,
                cause = null)

        doAnswer { throw expected }.whenever(groupServiceValidation).validateGetGroupsForUser(input)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupService.getGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupServiceValidation).validateGetGroupsForUser(input)
        verifyNoMoreInteractions(groupServiceValidation)
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `Delete Group, happy path - Deletes entity in database`() {
        val input = uuidString
        val group = GroupModel(id = uuidString, name = "Don Draper", userId = uuidString, color = "red")

        whenever(groupServiceValidation.validateModifyGroup(input)).then{/*do nothing*/}
        whenever(groupDao.deleteGroup(uuidString)).thenReturn(group)

        // act
        groupService.deleteGroup(input)

        // assert
        verify(groupServiceValidation).validateModifyGroup(input)
        verify(groupDao).deleteGroup(uuidString)
        verifyNoMoreInteractions(groupServiceValidation, groupDao)
    }

    @Test
    fun `Delete Group, user does not exist - throw not found`() {
        val input = uuidString
        val expected = NotFoundException(ErrorMessages.NOT_FOUND)

        whenever(groupServiceValidation.validateModifyGroup(input)).then{/*do nothing*/}
        whenever(groupDao.deleteGroup(uuidString)).thenReturn(null)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupService.deleteGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupServiceValidation).validateModifyGroup(input)
        verify(groupDao).deleteGroup(uuidString)
        verifyNoMoreInteractions(groupServiceValidation, groupDao)
    }

    @Test
    fun `Delete Group, invalid input - throw not found`() {
        val input = uuidString
        val expected = NotFoundException(ErrorMessages.NOT_FOUND)

        whenever(groupServiceValidation.validateModifyGroup(input)).then{/*do nothing*/}
        whenever(groupDao.deleteGroup(uuidString)).thenReturn(null)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupService.deleteGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        verify(groupServiceValidation).validateModifyGroup(input)
        verify(groupDao).deleteGroup(uuidString)
        verifyNoMoreInteractions(groupServiceValidation, groupDao)
    }
}