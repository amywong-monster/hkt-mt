package org.jinilover.hkt

import cats.implicits._

object TraversableExample {
  List(Option(2), None).sequence
//  val res1: Option[List[Int]] = None

  List(Option(2), Option(4)).sequence
//  val res2: Option[List[Int]] = Some(List(2, 4))

  List(Right(2), Left("Not an integer")).sequence
//  val res4: scala.util.Either[String,List[Int]] = Left(Not an integer)

  List[Either[String, Int]](Right(2), Right(4)).sequence
//  val res14: Either[String,List[Int]] = Right(List(2, 4))
}
