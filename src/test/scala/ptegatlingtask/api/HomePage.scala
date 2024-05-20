package ptegatlingtask.api

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import ptegatlingtask.config.BaseHelper.pteBaseUri
import ptegatlingtask.config.ProductDetails

object HomePage {
    def openPTEApplication(): ChainBuilder = {
        exec(http("Open Performance Testing Essentials Application").get(pteBaseUri))
        .exec(
            session => {
                val productsList: List[ProductDetails] = List()
                val newSession = session.set("productsList", productsList)

                newSession
            }
        )
    }
}
