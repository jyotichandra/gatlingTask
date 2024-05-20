package demoblaze.simulations

import demoblaze.api._
import demoblaze.config.BaseHelper._

import io.gatling.core.structure._
import io.gatling.http.Predef._
import io.gatling.core.Predef._

class Demoblaze extends Simulation {
    val scenario1: ScenarioBuilder = {
        scenario("Open Demoblaze and navigate to all Categories")
            .exec(flushHttpCache)
            .exec(flushCookieJar)
            .exitBlockOnFail(
                group("Home Page") {
                    exec(Home.openApplication())
                        .exec(Home.index())
                        .exec(thinkTimer())
                }
                .group("Category - Phone") {
                    exec(Category.navigateToCategory()).exec(thinkTimer())
                }
                .group("Category - Notebook") {
                    exec(Category.navigateToCategory("notebook")).exec(thinkTimer())
                }
                .group("Category - Monitor") {
                    exec(Category.navigateToCategory("monitor")).exec(thinkTimer())
                }
            )
    }

    // mvn gatling:test

    setUp(
        scenario1.inject(atOnceUsers(1))
    ).protocols(httpProtocol)
}
