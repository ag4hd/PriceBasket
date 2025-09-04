package com.assignment.pricebasket
package Fixtures

object TestFixtures {

  val pricesFixture: Map[String, BigDecimal] = Map(
    "soup" -> BigDecimal(0.65),
    "bread" -> BigDecimal(0.80),
    "milk" -> BigDecimal(1.30),
    "apples" -> BigDecimal(1.00)
  )

  val rulesFixture: List[DiscountRules] = List(
    DiscountRules.PercentageOff("apples", BigDecimal(10)), // 10% off apples
    DiscountRules.BuyAGetDiscountOnB("soup", 2, "bread", BigDecimal(50)) // buy 2 soup -> bread 50% off
  )
  
}
