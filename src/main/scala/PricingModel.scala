package com.assignment.pricebasket

object PricingModel {

  def subtotal(basketItems: Map[String, Int], itemPrice: Map[String, BigDecimal]): BigDecimal = {
    basketItems.map({ case (item, qty) => itemPrice(item) * qty }).sum
  }

  def appliedDiscounts(
    basketItemsWithQty: Map[String, Int],
    itemPrice: Map[String, BigDecimal],
    discountRules: List[DiscountRules]
  ): List[(String, BigDecimal)] = {
    discountRules.flatMap {
      case DiscountRules.PercentageOff(item, percentage) =>
        basketItemsWithQty.get(item).map { qty =>
          val discount = itemPrice(item) * (percentage / 100) * qty
          s"${item.capitalize} $percentage% off" -> discount
        }
      case DiscountRules.BuyAGetDiscountOnB(qualifierItem, qualifierQty, targetItem, targetDiscount) =>
        for {
          qualifierCount <- basketItemsWithQty.get(qualifierItem)
          targetCount    <- basketItemsWithQty.get(targetItem)
          applicableDiscounts = qualifierCount / qualifierQty
          discountsToApply = Math.min(applicableDiscounts, targetCount)
          if discountsToApply > 0
        } yield {
          val discount = itemPrice(targetItem) * (targetDiscount / 100) * discountsToApply
          s"${targetItem.capitalize} $targetDiscount% off" -> discount
        }
    }
  }

  def totalPrice(subTotal: BigDecimal, totalDiscount: BigDecimal): BigDecimal = {
    subTotal - totalDiscount
  }

  def fmt(m: BigDecimal): String = f"£$m%.2f"
}
