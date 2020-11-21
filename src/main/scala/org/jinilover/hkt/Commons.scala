package org.jinilover.hkt

object Commons {
  case class LowerCaseEvenNumber(quotient: Int, str: String)

  val convertToString: Int => String = {
    case 0 => "zero"
    case 1 => "one"
    case 2 => "Two"
    case 3 => "three"
    case 4 => "four"
    case 5 => "Five"
    case 6 => "six"
    case 7 => "seven"
    case 8 => "eight"
    case 9 => "Nine"
    case _ => "I give up"
  }
}
