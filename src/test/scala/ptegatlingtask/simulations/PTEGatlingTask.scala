package ptegatlingtask.simulations

import io.gatling.core.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.scenarios.PTEGatlingTaskScenarios._

class PTEGatlingTask extends Simulation {

    // mvn clean gatling:test

    setUp(
        pteGatlingTaskScenario.inject(atOnceUsers(100))
    ).protocols(httpProtocol)
}
