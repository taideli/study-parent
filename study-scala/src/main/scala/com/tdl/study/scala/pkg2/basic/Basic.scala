package com.tdl.study.scala.pkg2.basic

import scala.annotation.tailrec

object Basic {
  def main(args: Array[String]): Unit = {
    test4()
  }

  def test1(): Unit = {
    // notes by taidl@2017/7/5_11:57 range的使用
    val range = 1 to 20 by 3
    range.foreach(println)
  }

  def test2(): Unit = {
    // notes by taidl@2017/7/5_11:58 偏函数
    val pf1: PartialFunction[Any, String] = {case s: String => "YES"}
    val pf2: PartialFunction[Any, String] = {case d: Double => "YES"}
    val pf = pf1.orElse(pf2)  // 也可写成 pf = pf1 orElse pf2
    def tryPF(x: Any, f: PartialFunction[Any, String]): String =
      try {f(x).toString} catch {case _: MatchError => "ERROR!"}
    def d(x: Any, f: PartialFunction[Any, String]) =
      f.isDefinedAt(x).toString
    println("      |  pf1 - String  |  pf2 - Double  |  pf-All")
    println("x     |  def? | pf1(x) | def?  | pf2(x) |  def? | pf(x)")
    println("=======================================================")
    List("str", 3.14, 10).foreach {x =>
      printf("%-5s | %-5s | %-6s | %-5s | %-6s | %-5s | %-6s\n",
        x.toString, d(x, pf1), tryPF(x, pf1), d(x, pf2), tryPF(x, pf2), d(x, pf), tryPF(x, pf2))
    }
  }

  def test3(): Unit = {
    // notes by taidl@2017/7/5_13:09 多参数列表
    def fun(value: Int, delta: Int)(f: (Int, Int) => Int): Int = f(value, delta)
    val v = fun(9, 10)((a, b) => a + b)
    val u = fun(9, 10) {(a, b) => a + b}
    println("v = " + v)
    println("u = " + u)
  }

  def test4(): Unit = {
    // notes by taidl@2017/7/5_13:34 在方法里定义方法
    // 计算阶乘
    def factorial(i: Int): Long = {
      // 让编译器告诉我们是否正确地实现了尾递归
      @tailrec
      def fact(s: Int, accumulator: Int): Long = {
        if (0 == s) 0
        else if (s == 1) accumulator
        else fact(s - 1, s * accumulator)
      }
      fact(i, 1)
    }

    // test
    (0 to 10 by 3) foreach(i => {print(s"$i! = "); println(factorial(i))})
  }
}
