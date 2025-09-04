package com.assignment.pricebasket

object ItemsListAndPricing {

  case class Catalog(itemPrice: Map[String, BigDecimal]) {
    def getPrice(item: String): Option[BigDecimal] = itemPrice.get(item)
    def getAllItems: Set[String] = itemPrice.keySet
  }

  // catalog with item prices
  val providedCatalog: Catalog = Catalog(
    Map(
    "soup" -> BigDecimal(0.65),
    "bread" -> BigDecimal(0.80),
    "milk" -> BigDecimal(1.30),
    "apples" -> BigDecimal(1.00)
    )
  )
}
