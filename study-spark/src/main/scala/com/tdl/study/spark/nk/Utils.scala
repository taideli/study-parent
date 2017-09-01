package com.tdl.study.spark.nk

object Utils {
  def safe[S, T](f: S => T): (S => Either[T, (S, Exception)]) = {
    new Function[S, Either[T, (S, Exception)]] with Serializable {
      override def apply(s: S): Either[T, (S, Exception)] = {
        try {
          Left(f(s))
        } catch {
          case e: Exception => Right((s, e))
        }
      }
    }
  }


}
