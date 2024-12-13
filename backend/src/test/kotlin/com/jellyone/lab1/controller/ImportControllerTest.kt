package com.jellyone.lab1.controller

import com.jellyone.lab1.domain.enums.ImportStatus
import com.jellyone.lab1.web.dto.ImportDto
import com.jellyone.lab1.web.dto.SignUpRequest
import com.jellyone.lab1.web.dto.auth.JwtRequest
import com.jellyone.lab1.web.dto.auth.JwtResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MinIOContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class ImportControllerTest {
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
    fun shouldGetAllImportsByUser() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/import?page=1&pageSize=10")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
    }

    @Test
    fun shouldGetAllImports() {
        registerAdminUser("admin")
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .accept(ContentType.JSON)
            .`when`()
            .get("/api/import?page=1&pageSize=10")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
    }

    @Test
    fun shouldImportFile() {
        val fileContent = """
            name,x,y,realHero,hasToothpick,mood,speed,weaponType,car.model,car.brand,car.color,car.cool
            Ryan,1,1,true,true,FRENZY,10,AXE,Lada,Kalina,Red,true
            Ryan2,2,2,false,false,FRENZY,20,AXE,Lada,Kalina,Red,true
        """.trimIndent()

        val file = MockMultipartFile("file", "test.csv", "text/csv", fileContent.toByteArray())

        val importDto = RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .multiPart("file", "test.csv", file.bytes)
            .`when`()
            .post("/api/import")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`(ImportDto::class.java)

        Assertions.assertEquals(ImportStatus.FINISHED, importDto.status)
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
}