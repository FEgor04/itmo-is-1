import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.Simulation
import org.springframework.mock.web.MockMultipartFile
import java.time.Duration

class ImportDataSimulation : Simulation() {

    private val baseUrl = "http://localhost:8080"

    // Need to be replaced with real token
    private val authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlZmVkb3JvdiIsImlkIjoxLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTczNDE4MDcwNywiZXhwIjoxNzM0MTg0MzA3fQ.F_Dfvjoe4yr9cWsf8ff_2_Q7cCj_Cov7o0jzwg2uz1S_efbHhHDg6VrG_P90CAfhTPQRZ-MyXmHpZ769ygJwoA"

    private val httpProtocol = http.baseUrl(baseUrl)
        .acceptHeader("application/json")

    private val fileContent = """
            name,x,y,realHero,hasToothpick,mood,speed,weaponType,car.model,car.brand,car.color,car.cool
            Ryan Gosling,1,1,true,true,FRENZY,10,AXE,Lada,Kalina,Red,true
            Ryan2,2,2,false,false,FRENZY,20,AXE,Lada,Kalina,Red,true
        """.trimIndent()

    private val scn = scenario("Import Data Scenario")
        .exec(
            http("Import Data Scenario")
                .post("/api/import")
                .header("Authorization", "Bearer $authToken")
                .header("Content-Type", "multipart/form-data")
                .bodyPart(
                    ByteArrayBodyPart(
                        "file",
                        fileContent.toByteArray(),
                    ).fileName("file.csv")
                )
                .asMultipartForm()
                .check(status().`is`(409))
        )


    init {
        setUp(
            scn.injectOpen(constantUsersPerSec(20.0).during(Duration.ofSeconds(15)))
        ).protocols(httpProtocol)
    }
}
