package demoblaze.config

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

object BaseHelper {
    val demoblaze = "https://www.demoblaze.com/"
    val apiDemoblaze = "https://api.demoblaze.com/"
    val hlsDemoblaze = "https://hls.demoblaze.com/"

    def thinkTimer(min: Int = 2, max: Int = 4): ChainBuilder = pause(min, max)

    val httpProtocol: HttpProtocolBuilder = http
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate, br, zstd")
        .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
        .upgradeInsecureRequestsHeader("1")
        .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
}
