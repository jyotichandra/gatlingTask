package ptegatlingtask.api

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.config.ProductDetails

object CheckoutPage {
    def selectCountry(): ChainBuilder = {
        exec(
            http("Select a Country")
                .post(pteAdminUri)
                .formParam("action", "ic_state_dropdown")
                .formParam("country_code", "${c_countryCode}")
                .formParam("state_code", "")
                .check(jsonPath("$..[?(@.label=='${state}')].value").saveAs("c_stateCode"))
        )
    }

    def enterDetailsAndPlaceOrder(): ChainBuilder = {
        /**
         * Creating a formParamSequence which contains a List of tuples
         * Here, each tuple represents a formParam containing a paramKey and its paramValue
         */
        exec(
            session => {
                val sessionProductsList = session("productsList").as[List[ProductDetails]]

                val productPricesFormParameters = sessionProductsList.map(product => (s"""product_price_${product.id}__""", product.price))

                val newSession = session.set("c_productPricesFormParameters", productPricesFormParameters)
                newSession
            }
        )
        .exec(
            http("Enter Details and Place Order")
                .post(pteCheckoutUri)
                .formParam("ic_formbuilder_redirect", "${c_redirectThankYou}")
                .formParam("cart_content", "${c_cartContent}")
                .formParamSeq("${c_productPricesFormParameters}")
                .formParam("total_net", "${c_totalPrice}")
                .formParam("trans_id", "${c_transactionID}")
                .formParam("shipping", "order")
                .formParam("cart_content", "${c_cartContent}")
                .formParam("cart_type", "order")
                .formParam("cart_inside_header_1", "<b>BILLING ADDRESS</b>")
                .formParam("cart_company", "${company}")
                .formParam("cart_name", "${name}")
                .formParam("cart_address", "${address}")
                .formParam("cart_postal", "${postalCode}")
                .formParam("cart_city", "${city}")
                .formParam("cart_country", "${c_countryCode}")
                .formParam("cart_state", "${c_stateCode}")
                .formParam("cart_phone", "${phone}")
                .formParam("cart_email", "${email}")
                .formParam("cart_comment", "${comment}")
                .formParam("cart_inside_header_2", "<b>DELIVERY ADDRESS</b> (FILL ONLY IF DIFFERENT FROM THE BILLING ADDRESS)")
                .formParam("cart_s_company", "")
                .formParam("cart_s_name", "")
                .formParam("cart_s_address", "")
                .formParam("cart_s_postal", "")
                .formParam("cart_s_city", "")
                .formParam("cart_s_country", "")
                .formParam("cart_s_state", "")
                .formParam("cart_s_phone", "")
                .formParam("cart_s_email", "")
                .formParam("cart_s_comment", "")
                .formParam("cart_submit", "Place Order")
                .check(status.in(200, 302))
        )
    }
}
