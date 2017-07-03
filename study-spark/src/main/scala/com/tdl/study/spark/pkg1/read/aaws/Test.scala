package com.tdl.study.spark.pkg1.read.aaws

import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.ml.recommendation.ALS.Rating
import org.apache.spark.sql.SparkSession

object Test {
  val spark = SparkSession.builder().appName("").getOrCreate()

  def main(args: Array[String]): Unit = {
    val rawUserArtistData = spark.read.textFile("/ds/user_artist_data.txt")
//    rawArtistData.map(_.split(' ')(0).toDouble).stat
//    rawArtistData.map(_.split(' ')(1).toDouble).stat

    val rawArtistData = spark.read.textFile("/ds/artist_data.txt")
    val artisstByID = rawArtistData.flatMap(line => {
      val (id, name) = line.span(_ != '\t')
      if (name.isEmpty) {None} else try {
        Some((id.toInt, name.trim))
      } catch {
        case _: NumberFormatException => None
      }
    })

    val rawArtistAlias = spark.read.textFile("/ds/artist_alias.txt")
    val artistAlias = rawArtistAlias.flatMap(line => {
      val tokens = line.split('\t')
      if (tokens(0).isEmpty) None else Some((tokens(0).toInt, tokens(1).toInt))
    }).collectAsList()

    val bArtistAlias = spark.sparkContext.broadcast(artistAlias)

    val trainData = rawArtistData.map(line => {
      val Array(userID, artistID, count) = line.split(' ').map(_.toInt)
      val finalArtistID = bArtistAlias.value.get(artistID) //getOrElse(artistID, artistID)
      Rating(userID, finalArtistID, count)
    }).cache() // cache 因为 ALS算法是迭代的，要多次访问该数据集

//    val model = ALS.train(trainData, 10, 5, 0.01, 1.0)
    val model = ALS.train(trainData.rdd, 10, 5)
  }
}
