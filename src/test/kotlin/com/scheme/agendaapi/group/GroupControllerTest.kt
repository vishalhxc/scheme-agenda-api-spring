package com.scheme.agendaapi.group

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.scheme.agendaapi.group.model.Group
import com.scheme.agendaapi.group.service.GroupService
import com.scheme.agendaapi.model.AgendaApiResponse
import com.scheme.agendaapi.util.wrapper.ValidationWrapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GroupControllerTest {

    @Mock
    private lateinit var groupService: GroupService

    @Mock
    private lateinit var validationWrapper: ValidationWrapper

    @InjectMocks
    private lateinit var groupController: GroupController

    @Test
    fun `Create Group, happy path - returns created group`() {
        val input = Group(
                name = "Juarez Cartel",
                userId = "gusfring",
                color = "tan")
        val expected = AgendaApiResponse(
                status = "201_CREATED",
                body = Group(
                        id = "id",
                        name = "Juarez Cartel",
                        userId = "gusfring",
                        color = "tan"))

        whenever(validationWrapper.validate(input)).then {/*do nothing*/ }
        whenever(groupService.createGroup(input)).thenReturn(expected.body)

        // act
        val actual = groupController.createGroup(input)

        // assert
        assertEquals(expected, actual)
        verify(validationWrapper).validate(input)
        verify(groupService).createGroup(input)
        verifyNoMoreInteractions(validationWrapper, groupService)
    }

    @Test
    fun `Get Groups for User, happy path - returns groups`() {
        val input = "hankschrader"
        val expected = AgendaApiResponse(
                status = "200_OK",
                body = listOf(
                        Group(id = "id", name = "DEA", userId = "hankschrader", color = "black"),
                        Group(id = "id", name = "Schrader Family", userId = "hankschrader", color = "yellow")))

        whenever(groupService.getGroupsForUser("hankschrader")).thenReturn(expected.body)

        // act
        val actual = groupController.getGroupsForUser(input)

        // assert
        assertEquals(expected, actual)
        verify(groupService).getGroupsForUser("hankschrader")
        verifyNoMoreInteractions(groupService)
        verifyNoInteractions(validationWrapper)
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
        verifyNoInteractions(validationWrapper)
    }
}