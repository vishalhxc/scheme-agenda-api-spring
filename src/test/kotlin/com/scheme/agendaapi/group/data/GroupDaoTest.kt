package com.scheme.agendaapi.group.data

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.util.wrapper.UUIDWrapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupDaoTest {
    @Mock private lateinit var groupRepository: GroupRepository
    @Mock private lateinit var uuidWrapper: UUIDWrapper
    @InjectMocks private lateinit var groupDao: GroupDao

    private val uuidString = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"
    private val uuid = UUID.fromString(uuidString)

    @Test
    fun `Create Group, happy path - Creates entity and saves to database`() {
        val input = GroupModel(name = "Josh Futturman", userId = uuidString, color = "blue")
        val expected = GroupModel(id = uuidString, name = "Josh Futturman", userId = uuidString, color = "blue")
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
    fun `Create Group, invalid UUID - throws NPE`() {
        val input = GroupModel(name = "Josh Futturman", userId = "not a valid uuid", color = "blue")
        val expected = NullPointerException()

        // act
        val actual = assertThrows(expected.javaClass) {
            groupDao.createGroup(input)
        }

        // assert
        verifyNoMoreInteractions(uuidWrapper)
        verifyNoInteractions(groupRepository)
    }

    @Test
    fun `Get Groups for User, happy path - Returns all groups for user`() {
        val input = uuidString
        val expected = listOf(
                GroupModel(id = uuidString, name = "Wolf", userId = uuidString, color = "orange"),
                GroupModel(id = uuidString, name = "Tiger", userId = uuidString, color = "purple"))

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
        val expected = listOf<GroupModel>()

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
    fun `Get Groups for User, invalid UUID - return emptyList`() {
        val input = "not a uuid :)"
        val expected = emptyList<GroupModel>()

        // act
        val actual = groupDao.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verifyNoInteractions(uuidWrapper, groupRepository)
    }

    @Test
    fun `Delete Group, happy path - Deletes entity in database`() {
        val input = uuidString
        val entity = GroupEntity(
                id = uuid,
                name = "vishal",
                userId = uuid,
                color = "orange"
        )

        whenever(groupRepository.findById(uuid)).thenReturn(Optional.of(entity))
        doNothing().whenever(groupRepository).delete(entity)

        // act
        groupDao.deleteGroup(input)

        // assert
        verify(groupRepository).findById(uuid)
        verify(groupRepository).delete(entity)
        verifyNoMoreInteractions(groupRepository)
        verifyNoInteractions(uuidWrapper)
    }

    @Test
    fun `Delete Group, invalid UUID - return null`() {
        val input = "not a uuid :)"
        val expected = null

        // act
        val actual = groupDao.deleteGroup(input)

        // assert
        assertEquals(expected, actual)
        verifyNoInteractions(uuidWrapper, groupRepository)
    }
}