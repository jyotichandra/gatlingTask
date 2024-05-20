package demoblaze.api

import demoblaze.config.BaseHelper.apiDemoblaze
import io.gatling.core.structure._
import io.gatling.http.Predef._
import io.gatling.core.Predef._

object Category {
    def navigateToCategory(category: String = "phone"): ChainBuilder = {
        exec(
            http(s"Navigate to $category Category")
                .post(apiDemoblaze + "/bycat")
                .body(StringBody(s"""{"cat":"$category"}""")).asJson
                .check(jsonPath("$.Items[0].cat").is(category))
        )
    }
}
