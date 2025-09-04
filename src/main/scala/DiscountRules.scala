package com.assignment.pricebasket

sealed trait DiscountRules {
    // Define common properties or methods for all discount rules
    def label: String
}

object DiscountRules {
  // Percentage off of a specific item
  final case class PercentageOff(item: String, percentage: BigDecimal) extends DiscountRules{
    override val label = s"${item.capitalize} $percentage% off"
  }

  // Buy A get B at a discount
  final case class BuyAGetDiscountOnB(
    itemA: String,
    quantityA: Int,
    itemB: String,
    discountB: BigDecimal
  ) extends DiscountRules {
    val label =
      s"${itemB.capitalize} $discountB% off"
  }

  //Default discount for assignment
  val applesDiscount: PercentageOff = PercentageOff("apples", BigDecimal(10))
  val breadDiscount: BuyAGetDiscountOnB = BuyAGetDiscountOnB("soup", 2, "bread", BigDecimal(50))
  
}
