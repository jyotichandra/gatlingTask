package ptegatlingtask.api

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import ptegatlingtask.config.BaseHelper._

object ChairsPage {
    def navigateToChairsPage(): ChainBuilder = {
        exec(http("Navigate to Chairs Page").get(pteChairsUri))
    }
}
