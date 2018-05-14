package collection

object TestList {

  def l1 = {
    val l1 = List(1, 2, 3, 4)
    l1.foreach(println)
  }

  def l2 = {
    val l1 = List(1, 2, 3, 4)
    val l2 = List(5, 6, 7, 8)

    val l = l1 ::: l2 // 把后面的连接到前面，生成一个新的List
    println(l)
  }

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

  def main(args: Array[String]): Unit = {
//    l1
//    l2
    l3
  }
}
