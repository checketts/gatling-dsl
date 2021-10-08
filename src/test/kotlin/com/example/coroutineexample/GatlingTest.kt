package com.example.coroutineexample

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import io.gatling.core.javaapi.*
import io.gatling.core.javaapi.CoreDsl.atOnceUsers
import io.gatling.http.javaapi.HttpDsl.*
import io.gatling.http.javaapi.HttpProtocolBuilder
import org.junit.jupiter.api.Test
import java.time.Duration

open class HttpSimulation(block: SimulationDsl.() -> Any) : Simulation() {
    init {
        val dsl = SimulationDsl()
        dsl.block()

        setUp(dsl.buildScenarios())
    }
}

open class SimulationDsl(
        var httpProtocol: HttpProtocolBuilder = http(),
        val scenarios: MutableList<ScenarioBuilder> = mutableListOf(),

) {
    fun http(httpDefaults: HttpProtocolBuilder.() -> HttpProtocolBuilder) {
        httpProtocol = httpProtocol.httpDefaults()
    }

    fun scenario(name: String, block: ScenarioBuilder.() -> ScenarioBuilder) {
        val aScenario = CoreDsl.scenario(name)
        scenarios.add(aScenario.block())
    }

    fun buildScenarios(): List<PopulationBuilder> {
        return scenarios.map { it.injectOpen(atOnceUsers(1)).protocols(httpProtocol) }
    }

    fun String.get(path: String) {
        scenarios.last().exec(http(this).get(path))
    }

}

class BasicSimulation : HttpSimulation({
    http {
        baseUrl("http://computer-database.gatling.io") // Here are the common headers
                .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .doNotTrackHeader("1")
                .acceptLanguageHeader("en-US,en;q=0.5")
                .acceptEncodingHeader("gzip, deflate")
                .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    }

    scenario("scenario name") {
        exec(http("request_1").get("/")) // Note that Gatling has recorded real time pauses
                .pause(7)
                .exec(http("request_2")["/computers?f=macbook"])
                .pause(2)
                .exec(http("request_3")["/computers/6"])
                .pause(3)
                .exec(http("request_4")["/"])
                .pause(2)
                .exec(http("request_5")["/computers?p=1"])
                .pause(Duration.ofMillis(670))
                .exec(http("request_6")["/computers?p=2"])
                .pause(Duration.ofMillis(629))
                .exec(http("request_7")["/computers?p=3"])
                .pause(Duration.ofMillis(734))
                .exec(http("request_8")["/computers?p=4"])
                .pause(5)
                .exec(http("request_9")["/computers/new"])
                .pause(1)
                .exec(http("request_10") // Here's an example of a POST request
                            .post("/computers") // Note the triple double quotes: used in Scala for protecting a whole chain of
                            // characters (no need for backslash)
                            .formParam("name", "Beautiful Computer")
                            .formParam("introduced", "2012-05-30")
                            .formParam("discontinued", "")
                            .formParam("company", "37"))
    }


})


class GatlingTest {

    @Test
    fun sim() {
        val properties = GatlingPropertiesBuilder().apply {
            simulationClass(BasicSimulation::class.java.canonicalName)
            noReports()
        }
        val resultCode = Gatling.fromMap(properties.build())
        println(resultCode)
    }

}
