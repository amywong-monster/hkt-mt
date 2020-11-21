package org.jinilover.hkt

import org.jinilover.hkt.Commons._

object SharedCodeExample {
  import cats.Monad
  // the following syntax is sugar to
  // def compute[M[_]](
  //    ma: M[Int])(
  //    zeroProblem: M[Int],
  //    unEvenProblem: M[Int],
  //    notAllLowerCaseProblem: M[LowerCaseEvenNumber]
  //  )(implicit m: Monad[M]): M[LowerCaseEvenNumber] = {
  def compute[M[_]: Monad]( // `M` is type constructor satisfies the `Monad` constraint
    ma: M[Int]
  )(
    zeroProblem: M[Int],
    unEvenProblem: M[Int],
    notAllLowerCaseProblem: M[LowerCaseEvenNumber]
  ): M[LowerCaseEvenNumber] = {
    // the following import enable it to call `flatMap` on M[Int]
    // if satisfies the `Monad` constraint
    // once it can call `flatMap`, it can use syntactic sugar of `for`
    import cats.syntax.all._

    for {
      divisor  <- ma
      quotient <- if (divisor == 0) zeroProblem else M.pure(1000 / divisor)
      _        <- if (quotient % 2 == 0) M.pure(quotient) else unEvenProblem
      str = convertToString(quotient)
      result   <-
        if (str.toLowerCase == str) M.pure(LowerCaseEvenNumber(quotient, str)) else notAllLowerCaseProblem
    } yield result
  }
}

object SharedCodeExampleUsage {
  // import implementation of Monad[Option] and Monad[Either[String, ]]
  import cats.instances.all._

  def computeForEither(either: Either[String, Int]): Either[String, LowerCaseEvenNumber] =
    SharedCodeExample.compute(ma = either)(
      zeroProblem = Left("Cannot be divided by zero"),
      unEvenProblem = Left("After dividing 1000, the quotient is not even number"),
      notAllLowerCaseProblem = Left("Quotient str is not all lower case")
    )
  computeForEither(Left("Not an integer"))
  computeForEither(Right(0))
  computeForEither(Right(30))
  computeForEither(Right(250))

  def computeForOption(option: Option[Int]): Option[LowerCaseEvenNumber] =
    SharedCodeExample.compute(ma = option)(
      zeroProblem = None,
      unEvenProblem = None,
      notAllLowerCaseProblem = None
    )
  computeForOption(None)
  computeForOption(Option(0))
  computeForOption(Option(30))
  computeForOption(Option(250))
}
