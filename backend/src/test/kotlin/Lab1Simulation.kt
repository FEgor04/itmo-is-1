import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.Simulation
import java.time.Duration

class PostHumanSimulation : Simulation() {

    private val baseUrl = "http://localhost:8080"

    // Need to be replaced with real token
    private val authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJpZCI6MSwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzI5NzYwNDEsImV4cCI6MTczMjk3OTY0MX0.sWsGhRKxnCZwZR4JPMt94ACD0SoVDq7tr5ug1WgezQobuKgDWJKO2spkHh_coDSrdUQOSVRD_NlWzPShrHDBPA"

    private val httpProtocol = http.baseUrl(baseUrl)
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val requestBody = """
        {
          "name": "Ryan Gosling",
          "carId": 1,
          "x": 1231,
          "y": 1231,
          "mood": "SORROW",
          "realHero": true,
          "hasToothpick": true,
          "impactSpeed": 55,
          "weaponType": "PISTOL"
        }
    """.trimIndent()

    private val scn = scenario("Post Human Scenario")
        .exec(
            http("Post Human Request")
                .post("/api/humans")
                .header("Authorization", "Bearer $authToken")
                .body(StringBody(requestBody))
                .check(status().`is`(409))
        )

    private val importScn = scenario("Import Data Scenario")

    init {
        setUp(
            scn.injectOpen(constantUsersPerSec(20.0).during(Duration.ofSeconds(15)))
        ).protocols(httpProtocol)
    }
}
