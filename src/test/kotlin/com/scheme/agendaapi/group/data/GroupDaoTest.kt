package com.scheme.agendaapi.group.data

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.util.UUIDWrapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupDaoTest {

    @Mock
    private lateinit var groupRepository: GroupRepository

    @Mock
    private lateinit var uuidWrapper: UUIDWrapper

    @InjectMocks
    private lateinit var groupDao: GroupDao

    private val uuid = UUID.fromString("f2ea0d36-7420-4de0-a748-5a26c7ef6487")

    @Test
    fun `Create Group, happy path - Creates entity and saves to database`() {
        val input = Group(name = "Josh Futturman", userId = uuid, color = "blue")
        val expected = Group(id = uuid, name = "Josh Futturman", userId = uuid, color = "blue")
        val mockEntity = GroupEntity(id = uuid, name = input.name, userId = input.userId, color = input.color)

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
    fun `Get Groups for User, happy path - Returns all groups for user`() {
        val input = uuid
        val expected = listOf(
                Group(id = uuid, name = "Wolf", userId = uuid, color = "orange"),
                Group(id = uuid, name = "Tiger", userId = uuid, color = "purple"))

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
        val input = uuid
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
    fun `Delete Group, happy path - Deletes entity in database`() {
        val input = uuid

        doNothing().whenever(groupRepository).deleteById(input)

        // act
        groupDao.deleteGroup(input)

        // assert
        verify(groupRepository).deleteById(uuid)
        verifyNoMoreInteractions(groupRepository)
        verifyNoInteractions(uuidWrapper)

    }
}