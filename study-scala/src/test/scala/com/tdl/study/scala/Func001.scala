package com.tdl.study.scala

/**
  * 类
  */
object Func001 {

  class Complex(real: Double, imaginary: Double) {
    def re() = real
    def im = imaginary

    override def toString: String =
      "" + re + (if (im < 0) "" else "+") + im + "i"
  }

  def main(args: Array[String]): Unit = {
    val c = new Complex(3, 5)
    println(c.re())
    println(c.im)
    println(c im)
    println(c)
  }

}
