package ptegatlingtask.simulations

import io.gatling.core.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.scenarios.PTEGatlingTaskScenarios._

import scala.concurrent.duration.DurationInt

class PTEGatlingJenkinsTask extends Simulation{
    setUp(
        pteGatlingJenkinsTaskScenario.inject(rampUsers(5).during(10.seconds))
    ).protocols(httpProtocol)
}
