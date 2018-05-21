package com.tdl.study.scala.collection

object TestSet {
  def t1 = {
    var set = Set("Boeing", "Airbus")
    set += "Lear"
    println(set.contains("Cessna"))
    println(set.contains("Cessna"))
  }

  def main(args: Array[String]): Unit = {
    t1
  }
}
