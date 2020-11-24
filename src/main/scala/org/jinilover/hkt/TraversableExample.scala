package org.jinilover.hkt


object TraversableExample {
  // import everything from cats including
  // - `Traverse[M[_]]` containing `sequence`
  // - implicit `Traverse[List]` instance
  // - syntax sugar to call `sequence` on List
  // - implicit `Monad[Option]` instance
  // - implicit `Monad[Either[String, ]]` instance
  import cats.implicits._

  // flip List[Option[Int]] to Option[List[Int]]
  List(Option(2), None).sequence
//  val res1: Option[List[Int]] = None

  // flip List[Option[Int]] to Option[List[Int]]
  List(Option(2), Option(4)).sequence
//  val res2: Option[List[Int]] = Some(List(2, 4))

  // flip List[Either[String, Int]] to Either[String, List[Int]]
  List(Right(2), Left("Not an integer")).sequence
//  val res4: scala.util.Either[String,List[Int]] = Left(Not an integer)

  // flip List[Either[String, Int]] to Either[String, List[Int]]
  List[Either[String, Int]](Right(2), Right(4)).sequence
//  val res14: Either[String,List[Int]] = Right(List(2, 4))
}
