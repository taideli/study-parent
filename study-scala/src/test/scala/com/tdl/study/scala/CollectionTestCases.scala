package com.tdl.study.scala

import org.junit.Test

class CollectionTestCases {

  /** list test */
  @Test
  def l1 = {
    val l1 = List(1, 2, 3, 4)
    l1.foreach(println)
  }

  /** list concat test */
  @Test
  def l2 = {
    val l1 = List(1, 2, 3, 4)
    val l2 = List(5, 6, 7, 8)

    val l = l1 ::: l2 // 把后面的连接到前面，生成一个新的List
    println(l)
  }

  /** list concat test cases */
  @Test
  def l3 = {
    val l = Nil ::: List(1, 2, 3, 4)
    val L = 0 :: l // 在前面加
    val LL = 0 +: l  // 在前面加
    val LLL = l :+ 5 // 在后面加  这两种符号记忆法：冒号靠近List
    //    val l = l1::l2 // 把一个元素添加到list前面 // 这样一般不是我们想要的
    println(l)
    println(L)
    println(LL)
    println(LLL)
  }

  /** set test */
  @Test
  def s1 = {
    var set = Set("Boeing", "Airbus")
    set += "Lear"
    println(set.contains("Cessna"))
    println(set.contains("Cessna"))
  }

  /** pair test */
  @Test
  def p1 = {
    val pair = (99, "john")
    println(pair._1)
    println(pair.swap)
  }

  /** map test*/
  @Test
  def m1()  {
    var capital = Map("US" -> "Washington", "France" -> "Paris")
    capital += ("China" -> "BeiJing")

    println(capital("China"))
  }


  def factorial(x: BigInt): BigInt = if (0 == x) 1 else x * factorial(x - 1)

  @Test
  def fe() = {
    val args = new Array[String](4)
    args.update(0, "hello")
    args(1) = "" // 赋值会转换成对update 方法的调用
    args.update(2, "every")
    args.update(3, "body")
    args.foreach(println)
  }

  def forLoop2: Unit = println("wq")

  def forLoop = {
    val args = Array("1", "2", "3", "4")
    for (arg <- args) println(arg)
    // 这个 to 实际上是Int的to方法
    // 另外用圆括号时 scala会把它转换成对apply方法的调用(apply方法可以有多个参数)
    // args(i) 等效于args.apply(i)
    //args.apply(i)
    for (i <- 0 to 3) println(args(i))
  }

  @Test
  def getclazz() = println(forLoop2.getClass.getCanonicalName)

}
