package com.scheme.agendaapi.group.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.scheme.agendaapi.exception.error.ErrorResponse
import com.scheme.agendaapi.group.Group
import com.scheme.agendaapi.group.model.GroupModel
import com.scheme.agendaapi.model.AgendaApiResponse
import com.scheme.agendaapi.util.ErrorMessages
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class GroupingIntegrationTest(
        @Autowired private val groupDao: Group.Dao
) {

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null
    private lateinit var mockMvc: MockMvc

    private val baseUrl = "/groups"
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val uuid = "f2ea0d36-7420-4de0-a748-5a26c7ef6487"

    private inline fun <reified T> deserializeJson(input: String): T {
        return objectMapper.readValue(input, object : TypeReference<T>() {})
    }

    @BeforeEach
    fun `set up tests`() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext!!)
                .build()
        cleanUpDatabase()
    }

    @AfterEach
    fun `tear down tests`() {
        cleanUpDatabase()
    }

    private fun cleanUpDatabase() {
        groupDao.getGroupsForUser(uuid)
                .forEach { group -> group.id?.let { groupDao.deleteGroup(it) } }
    }

    private fun GroupModel.withoutId(): GroupModel {
        return GroupModel(
                id = null,
                name = this.name,
                userId = this.userId,
                color = this.color
        )
    }

    @Test
    fun `Create Group, creates group and returns 201 with response`() {
        val input = GroupModel(
                name = "Juarez Cartel",
                userId = uuid,
                color = "tan")
        val expected = AgendaApiResponse(
                status = "201 Created",
                body = GroupModel(
                        name = "Juarez Cartel",
                        userId = input.userId,
                        color = "tan"))
        val status = MockMvcResultMatchers.status().isCreated

        // act
        val actual = deserializeJson<AgendaApiResponse<GroupModel>>(
                mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
        assertEquals(expected.body!!.withoutId(), actual.body!!.withoutId())
    }

    @Test
    fun `Create Group, no name and invalid userId, returns 400`() {
        val input = GroupModel(userId = "blah")
        val expected = ErrorResponse(
                status = "400 Bad request",
                message = ErrorMessages.VALIDATION_ERROR,
                errors = listOf(
                        ErrorMessages.requiredField("name"),
                        ErrorMessages.invalidField("userId", "blah")))
        val status = MockMvcResultMatchers.status().isBadRequest

        // act
        val actual = deserializeJson<ErrorResponse>(
                mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
        assertEquals(expected.message, actual.message)
        assertEquals(expected.errors!!.count(), actual.errors!!.count())
        assertTrue(actual.errors!!.containsAll(expected.errors!!))
    }

    @Test
    fun `Create Group, group exists for user, returns 409`() {
        val input = GroupModel(
                name = "Juarez Cartel",
                userId = uuid,
                color = "tan")
        val expected = ErrorResponse(
                status = "409 Conflict",
                message = ErrorMessages.VALIDATION_ERROR,
                errors = listOf(Group.Constants.groupConflict(input.userId, input.name)))
        val status = MockMvcResultMatchers.status().isConflict

        groupDao.createGroup(input)

        // act
        val actual = deserializeJson<ErrorResponse>(
                mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
        assertEquals(expected.message, actual.message)
        assertEquals(expected.errors!!, actual.errors!!)
    }

    @Test
    fun `Get Groups For User, groups exist for user, returns 200 with groups`() {
        val input = uuid
        val groups = listOf(
                GroupModel(
                        name = "Juarez Cartel",
                        userId = uuid,
                        color = "tan"),
                GroupModel(
                        name = "DEA",
                        userId = uuid,
                        color = "blue")
        )
        val expected = AgendaApiResponse(
                status = "200 Ok",
                body = groups)
        val status = MockMvcResultMatchers.status().isOk

        groups.forEach { groupDao.createGroup(it) }

        // act
        val actual = deserializeJson<AgendaApiResponse<List<GroupModel>>>(
                mockMvc.perform(get("$baseUrl/users/$input")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
        assertTrue(actual.body!!.map { it.withoutId() }
                .containsAll(expected.body!!.map { it.withoutId() }))
    }

    @Test
    fun `Get Groups For User, no groups exist for user, returns 404`() {
        val input = "non existent user"
        val expected = ErrorResponse(
                status = "404 Not found",
                message = ErrorMessages.NOT_FOUND)
        val status = MockMvcResultMatchers.status().isNotFound

        // act
        val actual = deserializeJson<ErrorResponse>(
                mockMvc.perform(get("$baseUrl/users/$input")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `Delete Group, deletes group and returns 204`() {
        val group = groupDao.createGroup(
                GroupModel(
                        name = "Juarez Cartel",
                        userId = uuid,
                        color = "tan"))
        val input = group.id
        val expected = AgendaApiResponse<Unit>(status = "204 No content")
        val status = MockMvcResultMatchers.status().isNoContent

        // act
        val actual = deserializeJson<AgendaApiResponse<Unit>>(
                mockMvc.perform(delete("$baseUrl/$input")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
    }

    @Test
    fun `Delete Group, group does not exist, returns 404`() {
        val input = uuid
        val expected = ErrorResponse(
                status = "404 Not found",
                message = ErrorMessages.NOT_FOUND)
        val status = MockMvcResultMatchers.status().isNotFound

        // act
        val actual = deserializeJson<ErrorResponse>(
                mockMvc.perform(delete("$baseUrl/$input")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
    }

    @Test
    fun `Update Group, updates only color and name, returns 200`() {
        val group = groupDao.createGroup(
                GroupModel(
                        name = "Juarez Cartel",
                        userId = uuid,
                        color = "tan"))
        val input = group.id
        val inputBody = GroupModel(
                name = "DEA",
                userId = "a4e9b517-72da-435b-9818-4d24d61051d6",
                color = "blue")

        val expected = AgendaApiResponse(
                status = "200 Ok",
                body = GroupModel(
                        id = group.id,
                        userId = group.userId,
                        name = inputBody.name,
                        color = inputBody.color))
        val status = MockMvcResultMatchers.status().isOk

        // act
        val actual = deserializeJson<AgendaApiResponse<GroupModel>>(
                mockMvc.perform(patch("$baseUrl/$input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputBody)))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `Update Group, group does not exist, returns 404`() {
        val input = uuid
        val inputBody = GroupModel(
                name = "DEA",
                userId = "a4e9b517-72da-435b-9818-4d24d61051d6",
                color = "blue")
        val expected = ErrorResponse(
                status = "404 Not found",
                message = ErrorMessages.NOT_FOUND)
        val status = MockMvcResultMatchers.status().isNotFound

        // act
        val actual = deserializeJson<ErrorResponse>(
                mockMvc.perform(patch("$baseUrl/$input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputBody)))
                        .andExpect(status)
                        .andReturn().response.contentAsString)

        // assert
        assertEquals(expected.status, actual.status)
    }
}