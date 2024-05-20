package ptegatlingtask.simulations

import io.gatling.core.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.scenarios.PTEGatlingTaskScenarios._

import scala.concurrent.duration.DurationInt

class PTEGatlingResponseTimeTest extends Simulation {
    setUp(
        pteGatlingResponseTimeTestScenario.inject(rampUsers(72).during(144.seconds))
    ).protocols(httpProtocol)
}
