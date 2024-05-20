package ptegatlingtask.simulations

import io.gatling.core.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.scenarios.PTEGatlingTaskScenarios._

import scala.concurrent.duration.DurationInt

class PTEGatlingCapacityTest extends Simulation {
    setUp(
        pteGatlingCapacityTestScenario.inject(
            incrementConcurrentUsers(10).times(50).eachLevelLasting(20.seconds).startingFrom(0)
        )
    ).protocols(httpProtocol)
}
