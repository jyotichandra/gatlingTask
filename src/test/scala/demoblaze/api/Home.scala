package demoblaze.api

import demoblaze.config.BaseHelper._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import io.gatling.core.Predef._

object Home {
    def openApplication(): ChainBuilder = {
        exec(http("Open Demoblaze Application").get(demoblaze))
    }

    def index(): ChainBuilder = {
        exec(http("index").get(hlsDemoblaze + "index.m3u8"))
    }
}
