package com

// class Test2$
object Test2 {


  def test() = {

    def sayHi(name: String): Unit = {
      println("-------------1---------------")
      println("Hi " + name)
      println("-------------1---------------")
    }

    sayHi("p1")
    this.sayHi("p2") // don't suggest
    Test2.sayHi("p2")
    new Test2().sayHi("p3")
  }

  def main(args: Array[String]): Unit = {
    test()
    //sayHi("amie")
  }

  private def sayHi(name: String): Unit = {
    println("-------------2---------------")
    println("Hi " + name)
    println("-------------2---------------")
  }
}

class Test2 {

  def sayHi(name: String): Unit = {
    println("-------------3---------------")
    println("Hi " + name)
    println("-------------3---------------")

  }

}