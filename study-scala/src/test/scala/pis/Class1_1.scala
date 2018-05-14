package pis

object Class1_1 {

  def t1_1_1()  {
    var capital = Map("US" -> "Washington", "France" -> "Paris")
    capital += ("China" -> "BeiJing")

    println(capital("China"))
  }


  def factorial(x: BigInt): BigInt = if (0 == x) 1 else x * factorial(x - 1)


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

  def main(args: Array[String]): Unit = {
//    t1_1_1()
//    println(factorial(30))
//    println("25".toInt)
    fe()
    forLoop // 定义时没有带参数列表的括号，所以调用时可以不用带括号, 这时其实是定义了一个函数变量
    forLoop2
    println(forLoop2.getClass.getCanonicalName)
  }
}
