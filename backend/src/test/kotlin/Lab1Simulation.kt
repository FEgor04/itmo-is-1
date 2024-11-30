import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.Simulation

class PostHumanSimulation : Simulation() {

    private val baseUrl = "http://localhost:8080"

    private val authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmciLCJpZCI6MSwicm9sZXMiOlsiQURNSU4iXSwiaWF0IjoxNzMyOTczMjcyLCJleHAiOjE3MzI5NzY4NzJ9.bOHgWO-GtfWOKIaApIz1PfSxuGjrkKw9--RDSvWE-s_WAvQBEr7vde_e09754YOP34FbVc92-cwzm-xwSODT0Q"

    private val httpProtocol = http.baseUrl(baseUrl)
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val requestBody = """
        {
          "name": "Ryan Gosling",
          "carId": 1,
          "x": 12,
          "y": 123,
          "mood": "SORROW",
          "realHero": true,
          "hasToothpick": true,
          "impactSpeed": 12,
          "weaponType": "AXE"
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

    init {
        setUp(
            scn.injectOpen(atOnceUsers(10))
        ).protocols(httpProtocol)
    }
}
