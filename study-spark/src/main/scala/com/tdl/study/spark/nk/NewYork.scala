package com.tdl.study.spark.nk

import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

class NewYork

object NewYork {
  val log = LoggerFactory.getLogger(getClass)
  def main(args: Array[String]): Unit = {
    val path = if (args.length == 0) {
      "hdfs:///taxidata/yellow_tripdata_2016-01.csv"
    } else {
      args(0)
    }
    log.info("using csv file: " + path)
    val spark = SparkSession.builder()
      .appName(getClass.getSimpleName)
      .getOrCreate()

    val data = spark.read
      .option("header", true.toString) //具体的选项可以在 CSVOptions.scala中查看
      .option("inferSchema", true.toString)
      .csv(path)


    data.head(10).foreach(println)

    data.createOrReplaceTempView("t")
    spark.table("t").schema.fields.foreach(field => {
      println(s"${field.name} ${field.dataType} ${field.nullable} ${field.metadata}")
    })
  }
}
