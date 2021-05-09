package com

import java.lang.annotation.Annotation

object Test3 {

  def func(name: String) = {
    println(name)
  }

  def main(args: Array[String]): Unit = {


    class MyFunc extends Function[String, Unit] {
      override def apply(v1: String): Unit = {
        println(v1)
      }
    }
    val func1 = new Function[String, Unit]() {
      override def apply(v1: String): Unit = {
        println(v1)
      }
    }
    func1("func1")


    class MyFunc1 extends ((String) => Unit) {
      override def apply(v1: String): Unit = {
        println(v1)
      }
    }
    val func2 = new Function[String, Unit]() {
      override def apply(v1: String): Unit = {
        println(v1)
      }
    }
    func2("func2")

    val func3 = new MyFunc
    func3("func3")

    val func4 = (v1: String) => println(v1)
    func4("func4")

  }


}


@FunctionalInterface
class myFunc5{

}
