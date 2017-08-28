package com.tdl.study.spark.nk

import org.apache.spark.sql.SparkSession

object NewYork {
  def main(args: Array[String]): Unit = {
    val path = if (args.length == 0) {
      "hdfs:///taxidata/yellow_tripdata_2016-01.csv"
    } else {
      args(0)
    }
    println("using csv file: " + path)
    val spark = SparkSession.builder()
      .appName(getClass.getSimpleName)
      .getOrCreate()

    val data = spark.read
      .option("header", "true") //具体的选项可以在 CSVOptions.scala中查看
      .option("inferSchema", true.toString)
      .csv(path)


    data.head(10).foreach(println)
  }
}
