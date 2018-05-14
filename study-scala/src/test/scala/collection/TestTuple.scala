package collection

object TestTuple {

  def t1 = {
    val pair = (99, "john")
    println(pair._1)
    println(pair.swap)
  }

  def main(args: Array[String]): Unit = {
    t1
  }
}
