package com.scheme.agendaapi.group.data

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.exception.BadRequestException
import com.scheme.agendaapi.group.data.dao.GroupDao
import com.scheme.agendaapi.group.data.entity.GroupEntity
import com.scheme.agendaapi.group.data.repository.GroupRepository
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.util.ErrorConstants
import com.scheme.agendaapi.util.ErrorMessageConstants
import com.scheme.agendaapi.util.wrapper.UUIDWrapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.IllegalArgumentException
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupDaoTest {

    @Mock
    private lateinit var groupRepository: GroupRepository

    @Mock
    private lateinit var uuidWrapper: UUIDWrapper

    @InjectMocks
    private lateinit var groupDao: GroupDao

    private val uuidString = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"
    private val uuid = UUID.fromString(uuidString)

    @Test
    fun `Create Group, happy path - Creates entity and saves to database`() {
        val input = Group(name = "Josh Futturman", userId = uuidString, color = "blue")
        val expected = Group(id = uuidString, name = "Josh Futturman", userId = uuidString, color = "blue")
        val mockEntity = GroupEntity(id = uuid, name = "Josh Futturman", userId = uuid, color = "blue")

        whenever(uuidWrapper.generateUUID()).thenReturn(uuid)
        whenever(groupRepository.save(mockEntity)).thenReturn(mockEntity)

        // act
        val actual = groupDao.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(uuidWrapper).generateUUID()
        verify(groupRepository).save(mockEntity)
        verifyNoMoreInteractions(uuidWrapper, groupRepository)
    }

    @Test
    fun `Create Group, invalid UUID - throws bad request`() {
        val input = Group(name = "Josh Futturman", userId = "not a valid uuid", color = "blue")
        val expected = BadRequestException(
                ErrorMessageConstants.UUID_ERROR,
                IllegalArgumentException(),
                listOf(ErrorConstants.INVALID_UUID))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupDao.createGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.errors, actual.errors)
        verifyNoMoreInteractions(uuidWrapper)
        verifyNoInteractions(groupRepository)
    }

    @Test
    fun `Get Groups for User, happy path - Returns all groups for user`() {
        val input = uuidString
        val expected = listOf(
                Group(id = uuidString, name = "Wolf", userId = uuidString, color = "orange"),
                Group(id = uuidString, name = "Tiger", userId = uuidString, color = "purple"))

        val mockEntities = listOf(
                GroupEntity(id = uuid, name = "Wolf", userId = uuid, color = "orange"),
                GroupEntity(id = uuid, name = "Tiger", userId = uuid, color = "purple"))

        whenever(groupRepository.findAllByUserId(uuid)).thenReturn(mockEntities)

        // act
        val actual = groupDao.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupRepository).findAllByUserId(uuid)
        verifyNoMoreInteractions(groupRepository)
        verifyNoInteractions(uuidWrapper)
    }

    @Test
    fun `Get Groups for User, no results - Returns empty list`() {
        val input = uuidString
        val expected = listOf<Group>()

        val mockEntities = listOf<GroupEntity>()

        whenever(groupRepository.findAllByUserId(uuid)).thenReturn(mockEntities)

        // act
        val actual = groupDao.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupRepository).findAllByUserId(uuid)
        verifyNoMoreInteractions(groupRepository)
        verifyNoInteractions(uuidWrapper)
    }

    @Test
    fun `Get Groups for User, invalid UUID - throws bad request`() {
        val input = "not a uuid :)"
        val expected = BadRequestException(
                ErrorMessageConstants.UUID_ERROR,
                IllegalArgumentException(),
                listOf(ErrorConstants.INVALID_UUID))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupDao.getGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.errors, actual.errors)
        verifyNoInteractions(uuidWrapper, groupRepository)
    }

    @Test
    fun `Delete Group, happy path - Deletes entity in database`() {
        val input = uuidString

        doNothing().whenever(groupRepository).deleteById(uuid)

        // act
        groupDao.deleteGroup(input)

        // assert
        verify(groupRepository).deleteById(uuid)
        verifyNoMoreInteractions(groupRepository)
        verifyNoInteractions(uuidWrapper)
    }

    @Test
    fun `Delete Group, invalid UUID - throws bad request`() {
        val input = "not a uuid :)"
        val expected = BadRequestException(ErrorMessageConstants.UUID_ERROR,
                IllegalArgumentException(),
                listOf(ErrorConstants.INVALID_UUID))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupDao.deleteGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.errors, actual.errors)
        verifyNoInteractions(uuidWrapper, groupRepository)
    }
}