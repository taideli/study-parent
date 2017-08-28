package com.tdl.study.spark.pkg1.read

import java.text.SimpleDateFormat

import com.esri.core.geometry.Point
import org.apache.spark.sql.SparkSession
import org.joda.time.{DateTime, Duration}

object NewYorkTripData {
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def nscala_time(): Unit = {
    val dt1 = new DateTime(2017, 8, 28, 14, 51)
    val dt2 = new DateTime(2017, 8 ,28, 15, 3)

    val dur = new Duration(dt1, dt2)

    println(dur.getStandardSeconds)
  }

  def main(args: Array[String]): Unit = {
//    val sparkConf = new SparkConf()
    val spark = SparkSession.builder()
      .appName(NewYorkTripData.getClass.getSimpleName)
      .getOrCreate()

    val taxiRow = spark.read.csv("hdfs:///taxidata/taxidata/yellow_tripdata_2016-01_3k_record.csv")
    val taxiHead = taxiRow.take(10)
    taxiHead.foreach(println)

    val safeParse = safe(parse)
    val taxiParsed = taxiRow.rdd.map(_.toString()).map(safeParse)
    taxiParsed.cache

    taxiParsed.map(_.isLeft).countByValue().foreach(println)

    val taxiBad = taxiParsed.collect({
      case t if t.isRight => t.right.get
    })
    taxiBad.collect().foreach(println)

    val taxiGood = taxiParsed.collect({
      case t if t.isLeft => t.left.get
    })
    taxiGood.cache()
    taxiGood.values.map(hours).countByValue().toList.sorted.foreach(println)
  }


  case class Trip(
    pickupTime: DateTime,
    dropoffTime: DateTime,
    pickupLocation: Point,
    dropoffLocation: Point)

  def point(longitude: String, latitude: String): Point = {
    new Point(longitude.toDouble, latitude.toDouble)
  }

  def parse(line: String): (String, Trip) = {
    val fields = line.split(',')
    val license = fields(1)
    val pickupTime = new DateTime(sdf.parse(fields(5)))
    val dropoffTime = new DateTime(sdf.parse(fields(6)))
    val pickupLocation = point(fields(10), fields(11))
    val dropoffLocation = point(fields(12), fields(13))
    val trip = Trip(pickupTime, dropoffTime, pickupLocation, dropoffLocation)
    (license, trip)
  }

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

  def hours(trip: Trip): Long = {
    val d = new Duration(trip.pickupTime, trip.dropoffTime)
    d.getStandardHours
  }
}