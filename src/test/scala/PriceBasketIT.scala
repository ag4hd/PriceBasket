package com.assignment.pricebasket

import org.scalatest.funsuite.AnyFunSuite
import DiscountRules.*

import java.io.{ByteArrayOutputStream, PrintStream}

class PriceBasketIT extends AnyFunSuite {

    private def runAppWithArgs(args: Array[String]): String = {
      val baos = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(baos)) {
        PriceBasket.main(args)
      }
      baos.toString("UTF-8").trim
    }

    test("Soup Soup Bread Apples" ) {
      // Given
      val args = Array("soup", "soup", "bread", "apples")
      // When
      val actualOutput = runAppWithArgs(args)
      val expectedOutput =
        """Subtotal: £3.10
          |Apples 10% off: 10p
          |Bread 50% off: 40p
          |Total price: £2.60""".stripMargin
      // Then
      assert(actualOutput == expectedOutput)
    }

    test("invalid items are ignored gracefully") {
      // Given
      val args = Array("soup", "soup", "bread", "banana")
      // When
      val actualOutput = runAppWithArgs(args)
      val expectedOutput =
        """The following items are not valid and will be ignored: banana
          |Valid items are: soup, bread, milk, apples
          |Subtotal: £2.10
          |Bread 50% off: 40p
          |Total price: £1.70""".stripMargin
      // Then
      assert(actualOutput == expectedOutput)
    }

    test("no offers available for 1 soup + bread") {
      // Given
      val args = Array("soup", "bread")
      // When
      val actualOutput = runAppWithArgs(args)
      val expectedOutput =
        """Subtotal: £1.45
          |(No offers available)
          |Total price: £1.45""".stripMargin
      // Then
      assert(actualOutput == expectedOutput)
    }
}
