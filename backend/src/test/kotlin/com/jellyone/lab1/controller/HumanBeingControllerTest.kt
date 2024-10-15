package com.jellyone.lab1.controller

import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.web.dto.*
import com.jellyone.lab1.web.dto.auth.JwtRequest
import com.jellyone.lab1.web.dto.auth.JwtResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class HumanBeingControllerTest {

    @LocalServerPort
    private var port: Int = 0

    companion object {

        private var jwtToken: String? = null

        @Container
        private val postgres = PostgreSQLContainer<Nothing>("postgres:16-alpine")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }


    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port"
        if (jwtToken == null) {
            registerTestUser()
            loginUser()
        }
    }

    fun registerTestUser() {
        val signUpRequest = SignUpRequest(username = "testuser", password = "password")
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(signUpRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/register")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    private fun loginUser() {
        val loginRequest = JwtRequest(username = "testuser", password = "password")
        val response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`(JwtResponse::class.java)

        // Save the JWT token for use in further requests
        jwtToken = response.accessToken
    }

    @Test
    fun shouldGetAllHumans() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/humans?page=1&pageSize=10&sortBy=id&sortDirection=asc")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
    }

    @Test
    fun shouldGetHumanById() {
        val createdHuman = createTestHuman()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/humans/${createdHuman.id}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("name", equalTo(createdHuman.name))
            .body("id", equalTo(createdHuman.id?.toInt()))
    }

    @Test
    fun shouldReturn404ForNonExistentHuman() {
        val nonExistentId = UUID.randomUUID().mostSignificantBits

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/humans/$nonExistentId")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun shouldCreateHuman() {
        val createDto = CreateHumanBeingDto(
            name = "John Doe",
            x = 12.34,
            y = 56.78,
            realHero = true,
            hasToothpick = false,
            carId = 1L,
            mood = Mood.APATHY,
            impactSpeed = 150,
            weaponType = WeaponType.PISTOL
        )

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .contentType(ContentType.JSON)
            .body(createDto)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/humans")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .contentType(ContentType.JSON)
            .body("name", equalTo("John Doe"))
            .body("realHero", equalTo(true))
            .body("car.id", equalTo(1))
    }

    @Test
    fun shouldUpdateHuman() {
        val createdHuman = createTestHuman()

        val updateDto = PutHumanBeingDto(
            id = createdHuman.id,
            name = "Jane Doe",
            x = 20.0,
            y = 30.0,
            realHero = false,
            hasToothpick = true,
            carId = 1L,
            mood = Mood.APATHY,
            impactSpeed = 120,
            weaponType = WeaponType.PISTOL
        )

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .body(updateDto)
            .accept(ContentType.JSON)
            .`when`()
            .put("/api/humans/${createdHuman.id}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("name", equalTo("Jane Doe"))
            .body("realHero", equalTo(false))
    }

    @Test
    fun shouldDeleteHuman() {
        val createdHuman = createTestHuman()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .accept(ContentType.JSON)
            .`when`()
            .delete("/api/humans/${createdHuman.id}")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/humans/${createdHuman.id}")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    private fun createTestHuman(): HumanBeingDto {
        val createdCar = createTestCar()

        val createDto = CreateHumanBeingDto(
            name = "Test Human",
            x = 0.0,
            y = 0.0,
            realHero = false,
            hasToothpick = true,
            createdCar.id,
            mood = Mood.APATHY,
            impactSpeed = 100,
            weaponType = WeaponType.PISTOL
        )

        val response = RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .contentType(ContentType.JSON)
            .body(createDto)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/humans")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .`as`(HumanBeingDto::class.java)

        return response
    }

    private fun createTestCar(): CarDTO {
        val createDto = CreateCarDTO(
            color = "Red",
            model = "Civic",
            brand = "Honda",
            cool = true
        )

        val response = RestAssured.given()
            .header("Authorization", "Bearer $jwtToken") // Add the JWT token to the header
            .contentType(ContentType.JSON)
            .body(createDto)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/cars")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .`as`(CarDTO::class.java)

        return response
    }
}