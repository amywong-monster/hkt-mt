package org.jinilover.hkt

import org.jinilover.hkt.Commons._

object EitherExample {
  def compute(intEither: Either[String, Int]): Either[String, LowerCaseEvenNumber] =
    for {
      dividsor     <- intEither
      quotient     <- if (dividsor == 0) Left("Cannot be divided by zero") else Right(1000 / dividsor)
      evenQuotient <- if (quotient % 2 == 0) Right(quotient)
                      else Left("After dividing 1000, the quotient is not even number")

      str = convertToString(evenQuotient)
      result       <- if (str.toLowerCase == str) Right(LowerCaseEvenNumber(evenQuotient, str))
                      else Left("Quotient str is not all lower case")
    } yield result
}

object OptionExample {
  def compute(intOpt: Option[Int]): Option[LowerCaseEvenNumber] =
    for {
      dividsor     <- intOpt
      quotient     <- if (dividsor == 0) None else Some(1000 / dividsor)
      evenQuotient <- if (quotient % 2 == 0) Some(quotient) else None

      str = convertToString(evenQuotient)
      result <- if (str.toLowerCase == str) Some(LowerCaseEvenNumber(quotient, str)) else None
    } yield result
}

object ExampleUsage {
  EitherExample.compute(Left("Not an integer"))
  EitherExample.compute(Right(0))
  EitherExample.compute(Right(30))
  EitherExample.compute(Right(250))

  OptionExample.compute(None)
  OptionExample.compute(Option(0)) // Option(0) is the same as Some(0)
  OptionExample.compute(Option(30))
  OptionExample.compute(Option(250))
}
