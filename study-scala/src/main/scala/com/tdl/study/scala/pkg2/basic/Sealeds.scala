package com.tdl.study.scala.pkg2.basic

/**
  * sealed 关键字限定所有子类必须在当前文件中，不得在其它文件中继承
  */
sealed abstract class Sealeds {
  class Sealeds1 extends Sealeds {}
  class Sealeds2 extends Sealeds {}
}
