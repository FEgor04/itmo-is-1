import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.Simulation
import org.springframework.mock.web.MockMultipartFile
import java.time.Duration

class ImportDataSimulation : Simulation() {

    private val baseUrl = "http://localhost:8080"

    // Need to be replaced with real token
    private val authToken =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJpZCI6MSwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzI5NzYwNDEsImV4cCI6MTczMjk3OTY0MX0.sWsGhRKxnCZwZR4JPMt94ACD0SoVDq7tr5ug1WgezQobuKgDWJKO2spkHh_coDSrdUQOSVRD_NlWzPShrHDBPA"

    private val httpProtocol = http.baseUrl(baseUrl)
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val fileContent = """
            name,x,y,realHero,hasToothpick,mood,speed,weaponType,car.model,car.brand,car.color,car.cool
            Ryan,1,1,true,true,FRENZY,10,AXE,Lada,Kalina,Red,true
            Ryan2,2,2,false,false,FRENZY,20,AXE,Lada,Kalina,Red,true
        """.trimIndent()

    val file = MockMultipartFile("file", "test.csv", "text/csv", fileContent.toByteArray())

    private val scn = scenario("Import Data Scenario")
        .exec(
            http("Import Data Scenario")
                .post("/api/import")
                .header("Authorization", "Bearer $authToken")
                .bodyPart(
                    ByteArrayBodyPart(
                        "file",
                        fileContent.toByteArray(),
                    )
                )
                .check(status().`is`(200))
        )


    private val importScn = scenario("Import Data Scenario")

    init {
        setUp(
            scn.injectOpen(constantUsersPerSec(20.0).during(Duration.ofSeconds(15)))
        ).protocols(httpProtocol)
    }
}
