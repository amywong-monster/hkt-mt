package org.jinilover.hkt

import org.jinilover.hkt.Commons.{ LowerCaseEvenNumber, convertToString }

object SharedCodeExample {
  import cats.Monad
  // the following syntax is sugar to
  // def compute[M[_]](
  //    ma: M[Int])(
  //    zeroProblem: M[Int],
  //    unEvenProblem: M[Int],
  //    notAllLowerCaseProblem: M[LowerCaseEvenNumber]
  //  )(implicit M: Monad[M]): M[LowerCaseEvenNumber] = {
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

  def computeForEither(either: Either[String, Int]): Either[String, LowerCaseEvenNumber] = {
    // import everything including implicit Monad[Option] instance
    import cats.implicits._

    SharedCodeExample.compute(ma = either)(
      zeroProblem = Left("Cannot be divided by zero"),
      unEvenProblem = Left("After dividing 1000, the quotient is not even number"),
      notAllLowerCaseProblem = Left("Quotient str is not all lower case")
    )
  }
  computeForEither(Left("Not an integer"))
  computeForEither(Right(0))
  computeForEither(Right(30))
  computeForEither(Right(250))

  def computeForOption(option: Option[Int]): Option[LowerCaseEvenNumber] = {
    // import everything including implicit Monad[Either[String, ]] instance
    import cats.implicits._

    SharedCodeExample.compute(ma = option)(
      zeroProblem = None,
      unEvenProblem = None,
      notAllLowerCaseProblem = None
    )
  }
  computeForOption(None)
  computeForOption(Option(0))
  computeForOption(Option(30))
  computeForOption(Option(250))

  import zio.IO
  def computeForZio(theZIO: IO[String, Int]): IO[String, LowerCaseEvenNumber] = {
    // import implicit instances of Monad[IO[String, ]]
    import zio.interop.catz._

    SharedCodeExample.compute(ma = theZIO)(
      zeroProblem = IO.fail("Cannot be divided by zero"),
      unEvenProblem = IO.fail("After dividing 1000, the quotient is not even number"),
      notAllLowerCaseProblem = IO.fail("Quotient str is not all lower case")
    )
  }
  computeForZio(IO.fail("Not an integer"))
  computeForZio(IO.effectTotal(0))
  computeForZio(IO.effectTotal(30))
  computeForZio(IO.effectTotal(250))

  def computeForMonadStack(
    stack: Either[String, Option[Int]]
  ): Either[String, Option[LowerCaseEvenNumber]] = {
    import cats.data.OptionT
    // import everything including implicit Monad[OptionT[EITHER]] instance
    import cats.implicits._

    type EITHER[A] = Either[String, A]

    val optionT = SharedCodeExample.compute(ma = OptionT(stack))(
      zeroProblem = OptionT[EITHER, Int](Left("Cannot be divided by zero")),
      unEvenProblem = OptionT[EITHER, Int](Left("After dividing 1000, the quotient is not even number")),
      notAllLowerCaseProblem =
        OptionT[EITHER, LowerCaseEvenNumber](Left("Quotient str is not all lower case"))
    )
    optionT.value
  }
  computeForMonadStack(Left("Wrong by purpose"))
  // val res11: Either[String,Option[org.jinilover.hkt.Commons.LowerCaseEvenNumber]] = Left(Wrong by purpose)
  computeForMonadStack(Right(None))
  // val res12: Either[String,Option[org.jinilover.hkt.Commons.LowerCaseEvenNumber]] = Right(None)
  computeForMonadStack(Right(Option(0)))
  // val res13: Either[String,Option[org.jinilover.hkt.Commons.LowerCaseEvenNumber]] = Left(Cannot be divided by zero)
  computeForMonadStack(Right(Option(30)))
  // val res15: Either[String,Option[org.jinilover.hkt.Commons.LowerCaseEvenNumber]] = Left(After dividing 1000, the quotient is not even number)
  computeForMonadStack(Right(Option(250)))
  // val res16: Either[String,Option[org.jinilover.hkt.Commons.LowerCaseEvenNumber]] = Right(Some(LowerCaseEvenNumber(4,four)))
}
