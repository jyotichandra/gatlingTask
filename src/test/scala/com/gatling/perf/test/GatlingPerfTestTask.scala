package com.gatling.perf.test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object BaseHelpers {
	val baseUrl = System.getProperty("baseUrl", "http://localhost")

	val httpProtocol = http
		.baseUrl(baseUrl)
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0")
		.disableCaching // Flush cache
		.disableWarmUp // Flush cookies

	def httpRequest(name: String, path: String) =
		http(name)
			.get(path)

	def postRequest(name: String, path: String, params: Seq[(String, String)]) =
		http(name)
			.post(path)
			.formParamSeq(params)
}

class GatlingPerfTestTask extends Simulation {

	import BaseHelpers._

	val scn = scenario("Gatling_Performance_Testing_Task")
		.exitBlockOnFail {
			group("Home Page Group") {
				exec(httpRequest("Home_Page_Request", "/"))
			}
		}
		.pause(1)
		.exitBlockOnFail {
			group("Tables Page Group") {
				exec(httpRequest("Tables_Page_Request", "/tables"))
					.pause(1)
					.exec(httpRequest("Table_Product_Details_Page", "/products/living-room-table8"))
			}
		}
		.pause(1)
		.exitBlockOnFail {
			exec(postRequest("Table_Add_To_Cart_Request", "/wp-admin/admin-ajax.php",
				Seq("action" -> "ic_add_to_cart",
					"add_cart_data" -> "current_product=121&cart_content=%7B%2291__%22%3A1%7D&current_quantity=1",
					"cart_widget" -> "0",
					"cart_container" -> "0")))
		}
		.pause(1)
		.exitBlockOnFail {
			group("Chairs Page Group") {
				exec(httpRequest("Chairs_Page_Request", "/chairs"))
					.pause(1)
					.exec(httpRequest("Chair_Product_Details_Page", "/products/modern-chair8"))
			}
		}
		.pause(1)
		.exitBlockOnFail {
			exec(postRequest("Chair_Add_To_Cart_Request", "/wp-admin/admin-ajax.php",
				Seq("action" -> "ic_add_to_cart",
					"add_cart_data" -> "current_product=91&cart_content=%7B%22121__%22%3A1%7D&current_quantity=1",
					"cart_widget" -> "0",
					"cart_container" -> "0")))
		}
		.pause(1)
		.exitBlockOnFail {
			exec(httpRequest("View_Cart_Request", "/cart"))
		}
		.pause(2)
		.exitBlockOnFail {
			group("Place Order Group") {
				exec(httpRequest("Place_Order_Request", "/checkout"))
			}
		}
		.pause(3)
		.exitBlockOnFail {
			exec(postRequest("State_Dropdown_Request", "/wp-admin/admin-ajax.php",
				Seq("action" -> "ic_state_dropdown",
					"country_code" -> "IN",
					"state_code" -> "")))
		}
		.pause(3)
		.exitBlockOnFail {
			group("Final Checkout Group") {
				exec(http("Final_Checkout_Request")
					.post("/checkout")
				)
					.exec(http("Thank_You_Page_Request").get("/thank-you"))
			}
		}

	setUp(
		scn.inject(
			nothingFor(5), // Start delay
			rampUsers(100) during (10) // Ramp up 100 users over 10 seconds
		).protocols(httpProtocol)
	)
}
