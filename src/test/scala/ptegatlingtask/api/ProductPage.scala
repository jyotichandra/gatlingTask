package ptegatlingtask.api

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import ptegatlingtask.config.BaseHelper._
import ptegatlingtask.config.ProductDetails

object ProductPage {
    def selectAProduct(product: String): ChainBuilder = {
        exec(
            http(s"Select a $product")
                .get(pteProductsUri + "${" + product + "}")
                .check(css("input[name='current_product']", "value").saveAs("productID"))
                .check(css("input[name='cart_content']", "value").saveAs("productContent"))
                .check(css("input[name='current_quantity']", "value").saveAs("productQuantity"))
                .check(css("td.price-value").saveAs("productPrice"))
        )
            .exec(
                session => {
                    val productID = session("productID").as[String]
                    val productContent = session("productContent").as[String]
                    val productQuantity = session("productQuantity").as[String]
                    val productPrice = session("productPrice").as[String].substring(1)

                    val sessionProductsList = session("productsList").as[List[ProductDetails]]

                    val newProduct = ProductDetails(productID, productContent, productQuantity, productPrice)
                    val updatedProductList = sessionProductsList :+ newProduct

                    val newSession = session.set("productsList", updatedProductList)
                    newSession
                }
            )
    }

    def addProductToCart(): ChainBuilder = {
        exec(
            http("Add Product to cart")
                .post(pteAdminUri)
                .formParam("action", "ic_add_to_cart")
                .formParam("add_cart_data", session => {
                    val sessionProductsList = session("productsList").as[List[ProductDetails]]
                    val product = sessionProductsList.last

                    s"current_product=${product.id}&cart_content=${product.content}&cart_quantity=${product.quantity}"
                })
                .formParam("cart_widget", "0")
                .formParam("cart_container", "0")
                .check(status.is(200))
                .check(substring("Added!").exists)
        )
    }
}
