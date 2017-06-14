package com.tdl.study.spark.pkg1.read

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Column, SparkSession}


object ReadFile {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    val spark = SparkSession.builder()
      .appName(getClass.getSimpleName).config(sparkConf).getOrCreate()

    val df = spark.read.csv("/donation/block*")
    df.filter(new Column("").startsWith("id")).show()
    spark.stop()
  }
}