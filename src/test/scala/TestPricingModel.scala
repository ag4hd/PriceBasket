package com.assignment.pricebasket

import org.scalatest.funsuite.AnyFunSuite
import Fixtures.TestFixtures._

class TestPricingModel extends AnyFunSuite {

  test("subtotal: one item") {
    // Given
    val basketItems = Map("milk" -> 1)

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture)

    // Then
    assert(sub == BigDecimal(1.30))
  }

  test("no offers: soup + bread") {
    // Given
    val basketItems = Map("soup" -> 1, "bread" -> 1)

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture) // 0.65 + 0.80 = 1.45
    val lines = PricingModel.appliedDiscounts(basketItems, pricesFixture, rulesFixture)
    val tot  = PricingModel.totalPrice(sub, lines.map(_._2).sum)

    // Then
    assert(sub == BigDecimal(1.45))
    assert(lines.isEmpty)                // no 2 soups -> no bread discount; no apples
    assert(tot == BigDecimal(1.45))
  }

  test("apples only: 10% off") {
    // Given
    val basketItems = Map("apples" -> 1)

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture) // 1.00
    val lines = PricingModel.appliedDiscounts(basketItems, pricesFixture, rulesFixture)
    val discount = lines.map(_._2).sum                  // 0.10
    val tot  = PricingModel.totalPrice(sub, discount)   // 0.90

    // Then
    assert(sub == BigDecimal(1.00))
    assert(discount == BigDecimal(0.10))
    assert(tot == BigDecimal(0.90))
  }

  test("buy 2 soups -> 1 bread half-price") {
    // Given
    val basketItems = Map("soup" -> 2, "bread" -> 1)

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture) // 0.65*2 + 0.80 = 2.10
    val lines = PricingModel.appliedDiscounts(basketItems, pricesFixture, rulesFixture)
    val discount = lines.map(_._2).sum                  // 0.40
    val total  = PricingModel.totalPrice(sub, discount)   // 1.70

    // Then
    assert(sub == BigDecimal(2.10))
    assert(discount == BigDecimal(0.40))
    assert(total == BigDecimal(1.70))
  }

  test("mixed: soups trigger bread + apples gets 10%") {
    // Given
    val basketItems = Map("soup" -> 2, "bread" -> 1, "apples" -> 1)

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture) // 0.65*2 + 0.80 + 1.00 = 3.10
    val lines = PricingModel.appliedDiscounts(basketItems, pricesFixture, rulesFixture)
    val discount  = lines.map(_._2).sum                  // 0.40 + 0.10 = 0.50
    val total   = PricingModel.totalPrice(sub, discount)   // 2.60

    // Then
    assert(sub == BigDecimal(3.10))
    assert(discount == BigDecimal(0.50))
    assert(total == BigDecimal(2.60))
  }

  test("multiple eligibility breads (soup x4, bread x3 -> 2 breads discounted)") {
    // Given
    val basketItems = Map("soup" -> 4, "bread" -> 3)

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture) // 2.60 + 2.40 = 5.00
    val lines = PricingModel.appliedDiscounts(basketItems, pricesFixture, rulesFixture)
    val discount = lines.map(_._2).sum // From SUT, just to check totals
    val total = PricingModel.totalPrice(sub, discount) // 4.20
    val expectedBreadDiscount = 2 * (0.80 * 0.50) // 2 * 0.40 = 0.80

    // Then
    assert(sub == BigDecimal(5.00)) // subtotal check
    assert(discount == BigDecimal(0.80)) // actual discount matches expected
    assert(discount == BigDecimal(expectedBreadDiscount))
    assert(total == BigDecimal(4.20)) // total price check
  }

  test("empty basketItems") {
    // Given
    val basketItems = Map.empty[String, Int]

    // When
    val sub = PricingModel.subtotal(basketItems, pricesFixture)
    val lines = PricingModel.appliedDiscounts(basketItems, pricesFixture, rulesFixture)
    val discount = lines.map(_._2).sum
    val total = PricingModel.totalPrice(sub, discount)

    // Then
    assert(sub == BigDecimal(0))
    assert(discount == BigDecimal(0))
    assert(total == BigDecimal(0))
  }

  test("formats zero") {
    assert(PricingModel.fmt(BigDecimal(0)) == "£0.00")
  }

  test("formats whole pounds with .00") {
    assert(PricingModel.fmt(BigDecimal(2)) == "£2.00")
    assert(PricingModel.fmt(BigDecimal(123)) == "£123.00")
  }

  test("keeps two decimals when provided") {
    assert(PricingModel.fmt(BigDecimal("3.10")) == "£3.10")
    assert(PricingModel.fmt(BigDecimal("0.90")) == "£0.90")
  }

  test("pads single decimal with trailing zero") {
    assert(PricingModel.fmt(BigDecimal("4.5")) == "£4.50")
  }

  test("rounds half up at 2dp") {
    // JScala formatter rounds half-up
    assert(PricingModel.fmt(BigDecimal("1.234")) == "£1.23")
    assert(PricingModel.fmt(BigDecimal("1.235")) == "£1.24")
    assert(PricingModel.fmt(BigDecimal("0.005")) == "£0.01")
  }

  test("large values") {
    assert(PricingModel.fmt(BigDecimal("1234567.89")) == "£1234567.89")
  }

  test("negative values (refunds/credits)") {
    assert(PricingModel.fmt(BigDecimal("-2")) == "£-2.00")
    assert(PricingModel.fmt(BigDecimal("-0.015")) == "£-0.02") // half-up
  }

}
