package com

object Reg1 {

  def main(args: Array[String]): Unit = {
    val pattern = "Scala".r
    val str = "scala is dsaf and cool"
    val str1 = for (m <- pattern.findFirstMatchIn(str)) yield m.group(1)
    println(str1.getOrElse(""))
  }
}
