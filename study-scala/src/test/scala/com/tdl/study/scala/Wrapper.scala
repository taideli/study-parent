package com.tdl.study.scala

class Wrapper(val underling: Int) extends AnyVal {
  def foo: Wrapper = new Wrapper(underling * 19)
}
