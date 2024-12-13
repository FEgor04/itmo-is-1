package com.jellyone.lab1.controller

import com.jellyone.lab1.web.dto.*
import com.jellyone.lab1.web.dto.auth.JwtRequest
import com.jellyone.lab1.web.dto.auth.JwtResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MinIOContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class CarControllerTest {

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

        @Container
        private val minio = MinIOContainer("minio/minio:RELEASE.2024-11-07T00-52-20Z")

        @DynamicPropertySource
        @JvmStatic
        fun configureMinioProperties(registry: DynamicPropertyRegistry) {
            registry.add("minio.host", minio::getS3URL)
            registry.add("minio.username", minio::getUserName)
            registry.add("minio.password", minio::getPassword)
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

    @Test
    fun shouldGetAllCars() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/cars?page=1&pageSize=10&sortBy=id&sortDirection=asc")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
    }

    @Test
    fun shouldGetCarById() {
        val createdCar = createTestCar()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/cars/${createdCar.id}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("model", equalTo(createdCar.model))
            .body("id", equalTo(createdCar.id?.toInt()))
    }

    // Тест на получение несуществующего автомобиля
    @Test
    fun shouldReturn404ForNonExistentCar() {
        val nonExistentId = 99999L

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/cars/$nonExistentId")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun shouldCreateCar() {
        val createDto = CreateCarDTO(
            color = "Red",
            model = "Civic",
            brand = "Honda",
            cool = true
        )

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .body(createDto)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/cars")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .contentType(ContentType.JSON)
            .body("model", equalTo("Civic"))
            .body("brand", equalTo("Honda"))
            .body("color", equalTo("Red"))
    }

    @Test
    fun shouldUpdateCar() {
        val createdCar = createTestCar()

        val updateDto = UpdateCarDTO(
            model = "Accord",
            brand = "Honda",
            color = "Blue",
            cool = false
        )

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .body(updateDto)
            .accept(ContentType.JSON)
            .`when`()
            .put("/api/cars/${createdCar.id}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("model", equalTo("Accord"))
            .body("color", equalTo("Blue"))
    }

    @Test
    fun shouldDeleteCar() {
        val createdCar = createTestCar()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .delete("/api/cars/${createdCar.id}")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/cars/${createdCar.id}")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    private fun createTestCar(): CarDTO {
        val createDto = CreateCarDTO(
            color = "Red",
            model = "Civic",
            brand = "Honda",
            cool = true
        )

        val response = RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
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

        jwtToken = response.accessToken
    }
}
