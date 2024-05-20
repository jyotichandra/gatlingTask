package ptegatlingtask.api

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.config.ProductDetails

object CartPage {
    def openCartPage(): ChainBuilder = {
        exec(
            http("Navigate to Cart page")
                .get(pteCartUri)
                .check(css("td.total_net").saveAs("c_totalPrice"))
                .check(css("input[name='trans_id']", "value").saveAs("c_transactionID"))
        )
        // creating the value of "cart_content" formParameter from the added products
        .exec(
            session => {
                val sessionProductsList = session("productsList").as[List[ProductDetails]]

                var cartContent = """{"""
                sessionProductsList.foreach(element => {
                    if(element == sessionProductsList.last) cartContent += s""""${element.id}__":${element.quantity}"""
                    else cartContent += s""""${element.id}__":${element.quantity},"""
                })
                cartContent += """}"""

                val newSession = session.set("c_cartContent", cartContent)
                newSession
            }
        )
        // creating the payload body for "Place Order" request
        .exec(
            session => {
                val sessionProductsList = session("productsList").as[List[ProductDetails]]
                val cartContent = session("c_cartContent").as[String]
                val totalPrice = session("c_totalPrice").as[String]
                val transactionID = session("c_transactionID").as[String]

                var placeOrderBody = """"""
                placeOrderBody += s"""cart_content=${cartContent}&"""
                sessionProductsList.foreach(element => {
                    placeOrderBody += s"""p_id[]=${element.id}__&"""
                    placeOrderBody += s"""p_quantity[]=${element.quantity}&"""
                })
                placeOrderBody += s"""total_net=${totalPrice}&"""
                placeOrderBody += s"""trans_id=${transactionID}&"""
                placeOrderBody += s"""shipping=order"""

                val newSession = session.set("c_placeOrderBody", placeOrderBody)
                newSession
            }
        )
    }

    def placeOrder(): ChainBuilder = {
        exec(
            http("Place Order")
                .post(pteCheckoutUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(StringBody("${c_placeOrderBody}"))
                .check(regex("""<option value="[A-Z]{2}" >${country} - (.*?)</option>""").saveAs("c_countryCode"))
                .check(css("input[name='ic_formbuilder_redirect']", "value").saveAs("c_redirectThankYou"))
        )
    }
}
