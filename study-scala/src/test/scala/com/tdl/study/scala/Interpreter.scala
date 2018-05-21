package com.tdl.study.scala

/**
  * scala 插值器
  * https://www.yiibai.com/scala/scala_strings.html
  */
object Interpreter {
  def main(args: Array[String]): Unit = {
    val name = "Deli Tai"
    val age: Int = 28

//    s 插值器
    println(s"name: $name, age: $age")
    println(s"5 + 7 = ${5 + 7}")
//    f 插值器
    println(f"$name%4s $age%2.2f")
//    raw 原始插值器
    println(raw"5 + 7 =\n     ${5 + 7}")
  }
}
