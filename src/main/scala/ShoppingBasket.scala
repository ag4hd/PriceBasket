package com.assignment.pricebasket

object ShoppingBasket {

  //Normalise the items
  def normalizeItemName(items: List[String]): List[String] =
    items.map(_.trim.toLowerCase)

  //validate against the catalog and filter out invalid items
  def validateItems(items: List[String], catalog: Map[String, BigDecimal]): (List[String], List[String]) = {
    val (validItems, invalidItems)    = items.partition(catalog.contains)
    (validItems, invalidItems)
  }

  //aggregate the items to get the quantity of each item in the basket
  def aggregateItems(validItems: List[String]): Map[String, Int] =
    validItems.groupBy(identity).view.mapValues(_.size).toMap

  //Build a Basket with item names and their quantities
  def getBasketItemsWithQuantity(items: List[String], catalog: Map[String, BigDecimal]) = {
    val normalizedItems               = normalizeItemName(items)
    val (validItems, invalidItems) = validateItems(normalizedItems, catalog)
    val finalListItems                = aggregateItems(validItems)
    (finalListItems, invalidItems)
  }

  //Capitalise during print ex: apple -> Apple
//  def capitaliseItemName(itemName: String): String = {
//    if (itemName.isEmpty) itemName
//    else itemName.charAt(0).toUpper + itemName.substring(1).toLowerCase
//  }

}


