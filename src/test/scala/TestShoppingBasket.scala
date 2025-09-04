package com.assignment.pricebasket

import com.assignment.pricebasket.Fixtures.TestFixtures.*
import org.scalatest.funsuite.AnyFunSuite

class TestShoppingBasket extends AnyFunSuite{

    test("normalize item names trims spaces and capitalizes first letter") {
      // Given
      val input = List("  soup", "BREAD ", "  Apples")
      // When
      val normalized = ShoppingBasket.normalizeItemName(input)
      // Then
      assert(normalized.sorted == List("apples", "bread", "soup"))
    }

    test("invalid item only to normalize") {
      // Given
      val input = List("  @@", "Nothing ", "  Random", ".df.lfsfkjnflsn")
      // When
      val normalized = ShoppingBasket.normalizeItemName(input)
      // Then
      assert(normalized.sorted == List(".df.lfsfkjnflsn", "@@", "nothing", "random"))
    }

    test("validate items separates valid and invalid items based on catalog") {
      // Given
      val items = List("soup", "bread", "banana", "milk", "123")
      // When
      val (valid, invalid) = ShoppingBasket.validateItems(items, pricesFixture)
      // Then
      assert(valid.sorted == List("bread", "milk", "soup"))
      assert(invalid.sorted == List("123", "banana"))
    }

    test("invalid item only to go to invalid list") {
      // Given
      val input = List("  @@", "nothing ", "  random", ".df.lfsfkjnflsn")
      // When
      val (valid, invalid) = ShoppingBasket.validateItems(input, pricesFixture)
      // Then
      assert(invalid.sorted == List("  @@", "  random", ".df.lfsfkjnflsn", "nothing "))
    }

    test("aggregate counts multiple quantities correctly") {
      // Given
      val items = List("Soup", "Soup", "Bread", "Milk", "Milk", "Milk")
      // When
      val aggregated = ShoppingBasket.aggregateItems(items)
      // Then
      assert(aggregated.toSeq.sorted == Map("Milk" -> 3, "Soup" -> 2, "Bread" -> 1).toSeq.sorted)
    }

    test("getBasketItemsWithQuantity integrates normalization, validation, and aggregation") {
      // Given
      val input = List(" soup ", "Soup", " bread", "BANANA")
      // When
      val (basketItems, invalidItems) = ShoppingBasket.getBasketItemsWithQuantity(input, pricesFixture)
      // Then
      assert(basketItems.toSeq.sorted == Map("bread" -> 1, "soup" -> 2).toSeq.sorted) // Valid items with quantities
      assert(invalidItems == List("banana")) // Invalid items detected
    }

    test("invalid item only and get it normalize") {
      // Given
      val input = List("  @@", "Nothing ", "  Random", ".df.lfsfkjnflsn")
      // When
      val (basketItems, invalidItems) = ShoppingBasket.getBasketItemsWithQuantity(input, pricesFixture)
      // Then
      assert(basketItems.isEmpty) // No valid items
      assert(invalidItems.sorted == List(".df.lfsfkjnflsn", "@@", "nothing", "random"))
    }

    test("empty input returns empty basket and no invalid items") {
      val input = List.empty[String]
      val (basketItems, invalidItems) = ShoppingBasket.getBasketItemsWithQuantity(input, pricesFixture)
      assert(basketItems.isEmpty)
      assert(invalidItems.isEmpty)
    }
}
