package org.makc1mm.demo.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.makc1mm.demo.BaseTest
import org.makc1mm.demo.entity.PersonEntityTest
import org.makc1mm.demo.entity.ProductEntityTest
import org.makc1mm.demo.exception.ApiError
import org.makc1mm.demo.model.PersonRequest
import org.makc1mm.demo.model.PersonResponse
import org.makc1mm.demo.model.ProductResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Description
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class PersonControllerTest : BaseTest() {
    @Autowired
    private val restTemplate = TestRestTemplate()

    private val personRequest = PersonRequest(
        username = "test1",
        email = "testemail1@gmail.com",
        phoneNumber = "89998887766"
    )

    private val person = PersonEntityTest(
        id = 1,
        email = "testemail@gmail.com",
        phoneNumber = "899988877788",
        username = "test",
    )

    private val product = ProductEntityTest(
        id = 1,
        name = "test name",
        description = "test description",
        price = 123,
        personId = person.id
    )

    @AfterEach
    fun tearDown() {
        truncatePersonTable()
        resetSequence()
    }

    @Description("Проверка создания пользователя")
    @Test
    fun `create person`() {
        val response = restTemplate.postForEntity(
            "/$baseUrl/create",
            createBody(personRequest),
            Any::class.java
        )

        val expectedResult = PersonEntityTest(
            id = 1,
            username = personRequest.username,
            email = personRequest.email,
            phoneNumber = personRequest.phoneNumber
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(listOf(expectedResult), getPersonsFromTable())
    }

    @Description("Если пользователь с таким username уже существует - при создании получаем ошибку")
    @Test
    fun `if username already exists, get an error`() {
        insertPerson(person)

        val response = restTemplate.postForEntity(
            "$baseUrl/create",
            createBody(personRequest.copy(username = person.username)),
            ApiError::class.java
        )

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals(
            ApiError("Person with username: '${person.username}' already exists"),
            response.body
        )
    }

    @Description("Проверка получения списка пользователей")
    @Test
    fun `get all persons`() {
        val persons = listOf(
            person,
            person.copy(
                id = 2,
                email = "testemail2@gmail.com",
                phoneNumber = "89998885544",
                username = "test2")
        )
        persons.forEach { insertPerson(it) }
        insertProduct(product)

        val response = restTemplate.getForEntity(
            "$baseUrl/get/all",
            Array<PersonResponse>::class.java
        )

        val expectedResult = listOf(
            PersonResponse(
                username = persons.first().username,
                email = persons.first().email,
                phoneNumber = persons.first().phoneNumber,
                products = listOf(
                    ProductResponse(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        username = persons.first().username
                    )
                )
            ),
            PersonResponse(
                username = persons.last().username,
                email = persons.last().email,
                phoneNumber = persons.last().phoneNumber,
                products = emptyList()
            )
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(expectedResult, response.body!!.toList())
    }

    @Description("Если пользователей нет - получаем пустой массив")
    @Test
    fun `if there aren't persons, get empty list`() {
        val response = restTemplate.getForEntity(
            "$baseUrl/get/all",
            Array<PersonResponse>::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(emptyList<PersonResponse>(), response.body!!.toList())
    }

    @Description("Проверка получения пользователя по username")
    @Test
    fun `get person by username`() {
        insertPerson(person)
        listOf(
            product,
            product.copy(id = 2)
        ).forEach { insertProduct(it) }

        val response = restTemplate.getForEntity(
            "$baseUrl/get/${person.username}",
            PersonResponse::class.java
        )

        val expectedResult = PersonResponse(
            username = person.username,
            email = person.email,
            phoneNumber = person.phoneNumber,
            products = listOf(
                ProductResponse(
                    id = product.id,
                    name = product.name,
                    description = product.description,
                    price = product.price,
                    username = person.username
                ),
                ProductResponse(
                    id = 2,
                    name = product.name,
                    description = product.description,
                    price = product.price,
                    username = person.username
                )
            )
        )
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(expectedResult, response.body)
    }

    @Description("Если при запросе пользователя такого пользователя нет - получаем ошибку")
    @Test
    fun `if there isn't such username, get an error`() {
        val response = restTemplate.getForEntity(
            "$baseUrl/get/${person.username}",
            ApiError::class.java
        )

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        Assertions.assertEquals(
            ApiError("Person with username: '${person.username}' not found"),
            response.body
        )
    }

    @Description("Проверка изменения email и phoneNumber у пользователя")
    @Test
    fun `change person's email and phoneNumber`() {
        insertPerson(person)

        val response = restTemplate.exchange(
            "$baseUrl/change/${person.username}?email=newemail@mail.com&phoneNumber=8999881122",
            HttpMethod.PUT,
            HttpEntity(null, null),
            PersonResponse::class.java
        )

        val expectedResponse = PersonResponse(
            username = person.username,
            email = "newemail@mail.com",
            phoneNumber = "8999881122",
            products = emptyList()
        )
        val expectedResult = person.copy(
            email = "newemail@mail.com",
            phoneNumber = "8999881122"
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        // проверка ответа
        Assertions.assertEquals(expectedResponse, response.body)
        // проверка изменения в бд
        Assertions.assertEquals(listOf(expectedResult), getPersonsFromTable())
    }

    @Description("Проверка удаления пользователя, если у него есть продукты")
    @Test
    fun `delete person if he has products`() {
        insertPerson(person)
        insertProduct(product)

        restTemplate.delete("$baseUrl/delete/${person.username}")

        Assertions.assertEquals(emptyList<PersonEntityTest>(), getPersonsFromTable())
    }

    private fun createBody(body: Any) = HttpEntity<String>(objectMapper.writeValueAsString(body), headers)

    private fun insertPerson(person: PersonEntityTest) {
        database.createStatement().executeUpdate(
            """
                INSERT INTO person VALUES (
                    ${person.id},
                    '${person.email}',
                    '${person.phoneNumber}',
                    '${person.username}'
                )
        """.trimIndent()
        )
    }

    private fun insertProduct(product: ProductEntityTest) {
        database.createStatement().executeUpdate(
            """
                INSERT INTO product VALUES (
                    ${product.id},
                    '${product.description}',
                    '${product.name}',
                    ${product.price},
                    ${product.personId}
                )
            """.trimIndent()
        )
    }

    private fun resetSequence() {
        database.createStatement().executeUpdate("ALTER SEQUENCE person_id_seq RESTART WITH 1")
    }

    private fun truncatePersonTable() {
        database.createStatement().executeUpdate("TRUNCATE TABLE person CASCADE")
    }

    private fun getPersonsFromTable(): List<PersonEntityTest> {
        val persons = mutableListOf<PersonEntityTest>()
        val rs = database.createStatement().executeQuery("SELECT * FROM person")
        while (rs.next()) {
            persons.add(
                PersonEntityTest(
                    id = rs.getLong("id"),
                    username = rs.getString("username"),
                    email = rs.getString("email"),
                    phoneNumber = rs.getString("phone_number")
                )
            )
        }
        return persons
    }
}