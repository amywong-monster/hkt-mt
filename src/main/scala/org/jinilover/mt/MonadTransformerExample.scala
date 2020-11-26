package org.jinilover.mt

import org.jinilover.hkt.Commons.{ LowerCaseEvenNumber, convertToString }
import cats.data.OptionT
// import everything from cats including
// - syntax sugar to use `for` (aka flatMap/map) on `OptionT`
import cats.implicits._

object MonadTransformerExample {
  val stackExample: Either[String, Option[Int]] = Right(Some(1))

  type EITHER[A] = Either[String, A]

  /**
   * This skeleton look like [[org.jinilover.hkt.SharedCodeExample.compute()]], can we reuse it??
   *
   * @param optionT
   * @return
   */
  def compute(optionT: OptionT[EITHER, Int]): OptionT[EITHER, LowerCaseEvenNumber] =
    for {
      d      <- optionT
      q      <- if (d == 0) OptionT[EITHER, Int](Left("Cannot be divided by zero"))
                else OptionT[EITHER, Int](Right(Some(1000 / d)))
      even   <- if (q % 2 == 0) OptionT[EITHER, Int](Right(Some(q)))
                else OptionT[EITHER, Int](Left("the quotient is not even number"))

      str = convertToString(even)
      result <- if (str.toLowerCase == str)
                  OptionT[EITHER, LowerCaseEvenNumber](Right(Some(LowerCaseEvenNumber(even, str))))
                else OptionT[EITHER, LowerCaseEvenNumber](Left("Quotient str is not all lower case"))
    } yield result
}
