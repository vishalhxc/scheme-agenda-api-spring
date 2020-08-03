package com.scheme.agendaapi.group.service

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.exception.BadRequestException
import com.scheme.agendaapi.exception.ConflictException
import com.scheme.agendaapi.exception.NotFoundException
import com.scheme.agendaapi.group.Group
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.util.ErrorMessages
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GroupValidationTest {
    @Mock
    private lateinit var groupDao: Group.Dao

    @InjectMocks
    private lateinit var groupValidation: GroupValidation

    @Test
    fun `validateCreateGroup, happy path - does not throw exception`() {
        val input = GroupModel(
                name = "vishal",
                userId = "f2ea0d36-7420-4de0-a748-5a26c7ef6487",
                color = "blue")

        whenever(groupDao.getGroupForUser(input.userId, input.name)).thenReturn(null)

        // act
        assertDoesNotThrow {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        verify(groupDao).getGroupForUser(input.userId, input.name)
        verifyNoMoreInteractions(groupDao)
    }

    @Test
    fun `validateCreateGroup, name not provided - throw bad request`() {
        val input = GroupModel(
                name = "",
                userId = "f2ea0d36-7420-4de0-a748-5a26c7ef6487",
                color = "")
        val expected = BadRequestException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(ErrorMessages.requiredField("name")))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateCreateGroup, user ID not provided - throw bad request`() {
        val input = GroupModel(
                name = "vishal",
                userId = "",
                color = "")
        val expected = BadRequestException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(ErrorMessages.requiredField("userId")))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateCreateGroup, user ID invalid - throw bad request`() {
        val input = GroupModel(
                name = "vishal",
                userId = "not a uuid",
                color = "blue")
        val expected = BadRequestException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(
                        ErrorMessages.invalidField("userId", input.userId)))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateCreateGroup, name and userId not provided - throw bad request`() {
        val input = GroupModel(
                name = "",
                userId = "",
                color = "")
        val expected = BadRequestException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(
                        ErrorMessages.requiredField("userId"),
                        ErrorMessages.requiredField("name")))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateCreateGroup, name not provided and user ID invalid - throw bad request`() {
        val input = GroupModel(
                name = "",
                userId = "not a uuid",
                color = "blue")
        val expected = BadRequestException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(
                        ErrorMessages.invalidField("userId", input.userId),
                        ErrorMessages.requiredField("name")))

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateCreateGroup, name and user ID combination exists - throw conflict`() {
        val input = GroupModel(
                name = "vishal",
                userId = "f2ea0d36-7420-4de0-a748-5a26c7ef6487",
                color = "blue")
        val expected = ConflictException(
                message = ErrorMessages.VALIDATION_ERROR,
                cause = null,
                errors = listOf(
                        Group.Constants.groupConflict(input.userId, input.name)
                ))

        whenever(groupDao.getGroupForUser(input.userId, input.name)).thenReturn(input)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateCreateGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verify(groupDao).getGroupForUser(input.userId, input.name)
        verifyNoMoreInteractions(groupDao)
    }

    @Test
    fun `validateModifyGroup, happy path - does not throw`() {
        val input = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"

        // act
        assertDoesNotThrow {
            groupValidation.validateModifyGroup(input)
        }

        // assert
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateModifyGroup, blank group ID - throw required exception`() {
        val input = ""
        val expected = NotFoundException(message = ErrorMessages.NOT_FOUND)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateModifyGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateModifyGroup, invalid group ID - throw invalid exception`() {
        val input = "hello"
        val expected = NotFoundException(message = ErrorMessages.NOT_FOUND)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateModifyGroup(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateGetGroupsForUser, happy path - does not throw`() {
        val input = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"

        // act
        assertDoesNotThrow {
            groupValidation.validateGetGroupsForUser(input)
        }

        // assert
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateGetGroupsForUser, blank user ID - throw required exception`() {
        val input = ""
        val expected = NotFoundException(message = ErrorMessages.NOT_FOUND)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateGetGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }

    @Test
    fun `validateGetGroupsForUser, invalid user ID - throw invalid exception`() {
        val input = "hello"
        val expected = NotFoundException(message = ErrorMessages.NOT_FOUND)

        // act
        val actual = assertThrows(expected.javaClass) {
            groupValidation.validateGetGroupsForUser(input)
        }

        // assert
        assertEquals(expected.message, actual.message)
        assertEquals(expected.cause, actual.cause)
        assertTrue(expected.errors.containsAll(actual.errors))
        verifyNoInteractions(groupDao)
    }
}