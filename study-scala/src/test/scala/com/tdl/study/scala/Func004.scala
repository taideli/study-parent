package com.tdl.study.scala

/**
  * 泛型
  */
object Func004 {
  class Reference[T] {
    private var contents: T = _
    def set(value: T) { contents = value }
    def get: T = contents
  }

  def main(args: Array[String]): Unit = {
    val r = new Reference[Int]
    r.set(33)
    println(r.get)
  }
}
