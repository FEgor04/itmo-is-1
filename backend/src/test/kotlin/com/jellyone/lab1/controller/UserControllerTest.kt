package com.jellyone.lab1.controller

import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.web.dto.SignUpRequest
import com.jellyone.lab1.web.dto.auth.JwtRequest
import com.jellyone.lab1.web.dto.auth.JwtResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class UserControllerTest {
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
    }


    @Test
    fun shouldNotAssignAdminRoleToAuthorizedUser() {
        val username = "admin2"
        val token = registerAdminUser(username)

        RestAssured.given()
            .header("Authorization", "Bearer $token")
            .contentType(ContentType.TEXT)
            .body(username)
            .`when`()
            .get("/api/amIAdmin")
            .then()
            .statusCode(500)
    }

    @Test
    fun shouldAssignAdminRoleToAuthorizedUser() {
        val username = "admin"
        jwtToken = registerAdminUser(username)
        submitAdminRequest()
        loginAdmin(username)

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.TEXT)
            .body(username)
            .`when`()
            .get("/api/amIAdmin")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("username", IsEqual.equalTo(username))
            .body("role", IsEqual.equalTo(Role.ADMIN.toString()))
    }

    private fun registerAdminUser(username: String): String {
        val signUpRequest = SignUpRequest(username, password = "password")
        val jwtResponse = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(signUpRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/register")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`(JwtResponse::class.java)
        return jwtResponse.accessToken;
    }

    private fun loginAdmin(username: String) {
        val loginRequest = JwtRequest(username, password = "password")
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

    private fun submitAdminRequest() {
        RestAssured.given()
            .header("Authorization", "Bearer ${jwtToken}")
            .contentType(ContentType.TEXT)
            .body("adminuser")
            .`when`()
            .post("/api/admin/requests/submit")
            .then()
            .statusCode(HttpStatus.OK.value())
    }
}