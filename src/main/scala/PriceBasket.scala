package com.assignment.pricebasket

import ItemsListAndPricing.*
import DiscountRules.*
import ShoppingBasket.*
import PricingModel.*

import scala.math.BigDecimal.RoundingMode

object PriceBasket {
  def main(args: Array[String]): Unit = {

    // Get catalog instance
    val catalog = providedCatalog

    //Get discount rules
    val discountRules = List(applesDiscount, breadDiscount)

    // Build set string of valid items for user information
    val validItemsString = catalog.getAllItems.mkString(", ")

    // Input from command line arguments
    val shoppingBasket: List[String] = args.toList
    if (shoppingBasket.isEmpty) {
      println("Please provide items in the shopping basket as command line arguments")
      println(s"Valid items are: $validItemsString")
      sys.exit(1)
    }
    // Get basket items with quantity and any invalid items
    val (basketItemsWithQty, invalidItems) = getBasketItemsWithQuantity(shoppingBasket, catalog.itemPrice)
    if (invalidItems.nonEmpty) {
      println(s"The following items are not valid and will be ignored: ${invalidItems.mkString(", ")}")
      println(s"Valid items are: $validItemsString")
    }

    //Price Calculation
    val subTotal: BigDecimal                         = PricingModel.subtotal(
      basketItemsWithQty, catalog.itemPrice
    )
    val appliedDiscounts: List[(String, BigDecimal)] = PricingModel.appliedDiscounts(
      basketItemsWithQty, catalog.itemPrice, discountRules
    )
    val totalDiscount: BigDecimal                    = appliedDiscounts.map(_._2).sum
    val finalPrice: BigDecimal                       = PricingModel.totalPrice(subTotal, totalDiscount)

    // Helper to format pounds or pence
    def formatAmount(amount: BigDecimal): String = {
      if (amount >= 1) f"£$amount%.2f"
      else s"${(amount * 100).setScale(0, RoundingMode.HALF_UP).toInt}p"
    }

    // --- Printing Output ---
    println("Subtotal: " + formatAmount(subTotal))

    if (appliedDiscounts.isEmpty) {
      println("(No offers available)")
    } else {
      appliedDiscounts.foreach {
        case (label, amount) =>
        println(s"$label: ${formatAmount(amount)}")
      }
    }

    println("Total price: " + formatAmount(finalPrice))
  }
}



