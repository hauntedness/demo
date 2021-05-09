package com


class Test {
  private val name: String = "rose"

  private val age: Int = 18

  private def sayHello() = {

    println("name: " + name)
    println("sex: " + Test.sex)
    val f = Test.test(1)
  }

  private def dance() = {

  }
}

object Test {
  private val name: String = "jack"

  private val sex: String = "male"

  def test(a : Int) = {
    val test = new Test()
    //test.sayHello()
    println(test.age)
    println(name)
  }

  def main(args: Array[String]): Unit = {
    test(1)
  }
}