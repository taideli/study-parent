package com.tdl.study.spark.nk

import java.text.SimpleDateFormat
import java.util.Locale

import com.esri.core.geometry.Point
import com.tdl.study.spark.nk.GeoJsonProtocol._
import org.apache.spark.sql.{Row, SparkSession}
import org.joda.time.{DateTime, Duration}
import org.slf4j.LoggerFactory
import spray.json._


object RunGeoTime extends Serializable with TripFieldIdx {
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
  private val log = LoggerFactory.getLogger(getClass)
  private val spark = SparkSession.builder().appName(getClass.getSimpleName).getOrCreate()

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println(
        """Usage:
          |    ${getClass.getSimpleName} <csv> <geojson>
        """.stripMargin)
      System.exit(1)
      }
    val taxiRaw = spark.read
      .option("header", true.toString)
      .option("inferSchema", true.toString)
//      .csv("hdfs:///taxidata/yellow_tripdata_2016-01.csv")
      .csv(args(0))     // job 1
    def safeParse = Utils.safe(parse)
//    val taxiParesd = taxiRaw.map(_.mkString(",")).map(safeParse)
    val taxiParsed = taxiRaw.rdd.map(safeParse)

    val taxiGood = taxiParsed.filter(_.isLeft).map(_.left.get)

    def hours(trip: Trip): Long = {
      val d = new Duration(trip.pickupTime, trip.dropoffTime)
      d.getStandardHours
    }
    println("====taxiGood count: " + taxiGood.count())
                              // job 2 countByValue
    taxiGood.values.map(hours).countByValue().toList.sorted.foreach(d => println("====" + d))
    taxiParsed.unpersist() // ?

    // 根据上面的输出结果，统计乘车时间大于0且小于3小时的记录
    val taxiClean = taxiGood.filter {
      case (_, trip) => val hrs = hours(trip); 0 < hrs && hrs < 3
    }

    /*获取纽约行政区划数据*/
//    val geojson = scala.io.Source.fromURL(getClass.getResource("/nyc-boroughs.geojson")).mkString
//    val geojson = scala.io.Source.fromURL(getClass.getResource("/taidl/sources/github/nyc-maps/boroughs.geojson")).mkString
    val geojson = scala.io.Source.fromFile(args(1)).mkString
    /*转换为地理要素*/
//    val geoMap = GeometryEngine.geometryFromGeoJson(geojson, GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Unknown)
//    val geometry = geoMap.getGeometry
//    val geoSRef = geoMap.getSpatialReference
//    RichGeometry.createRichGeometry(geometry).contains(taxiGood.first()._2.dropoffLocation)
    implicit val formatter = FeatureJsonFormat
    val features = geojson.parseJson.convertTo[FeatureCollection]
    val areaSortedFeatures = features.sortBy(feature => {
      val borough = feature("boroughCode").convertTo[Int]
      (borough, -feature.geometry.area2D())
    })
    val bFeatures = spark.sparkContext.broadcast(areaSortedFeatures)

    def borough(trip: Trip): Option[String] = {
      val feature: Option[Feature] = bFeatures.value.find(feature => {
        feature.geometry.contains(trip.dropoffLocation)
      })
      feature.map(f => {f("borough").convertTo[String]})
    }

    println("======================================================taxiClean:" + taxiClean.count())
    taxiClean.values.map(borough).countByValue().foreach(d => println("===" + d))

    /*调试*/
    val taxiDone = taxiClean.filter({case (_, trip) => !hasZero(trip)})
    println("======================================================taxiDone:" + taxiDone.count())
    taxiDone.values.map(borough).countByValue().foreach(d => println("===" + d))

    taxiGood.unpersist()

    spark.stop()
  }

  def parse(row: Row): (String, Trip) = {
    val vendorID = row.getInt(VENDORID)
    val pickupDatetime = new DateTime(sdf.parse(row.get(TPEP_PICKUP_DATETIME).toString))
    val dropoffDatetime = new DateTime(sdf.parse(row.get(TPEP_DROPOFF_DATETIME).toString))
    val passengerCount = row.getInt(PASSENGER_COUNT)
    val tripDistance = row.getDouble(TRIP_DISTANCE)
    val pickupLocation = point(row.getDouble(PICKUP_LONGITUDE), row.getDouble(PICKUP_LATITUDE))
    val ratecodeID = row.getInt(RATECODEID)
    val storeAndFwdFlag = if (row.getString(STORE_AND_FWD_FLAG).toUpperCase.equals("Y")) true else false
    val dropoffLocation = point(row.getDouble(DROPOFF_LONGITUDE), row.getDouble(DROPOFF_LATITUDE))
    val paymentType = row.getInt(PAYMENT_TYPE)
    val fareAmount = row.getDouble(FARE_AMOUNT)
    val extra = row.getDouble(EXTRA)
    val mtaTax = row.getDouble(MTA_TAX)
    val tipAmount = row.getDouble(TIP_AMOUNT)
    val tollsAmount = row.getDouble(TOLLS_AMOUNT)
    val improvementSurcharge = row.getDouble(IMPROVEMENT_SURCHARGE)
    val totalAmount = row.getDouble(TOTAL_AMOUNT)
    (vendorID.toString, Trip(pickupDatetime, dropoffDatetime, pickupLocation, dropoffLocation))
  }

  /*def parse(line: String): (String, Trip) = {
    val fields = line.split(',')
    val license = fields(1)
    val pickupTime = new DateTime(sdf.parse(fields(5)))
    val dropoffTime = new DateTime(sdf.parse(fields(6)))
    val pickupLocation = point(fields(10), fields(11))
    val dropoffLocation = point(fields(12), fields(13))
    val trip = Trip(pickupTime, dropoffTime, pickupLocation, dropoffLocation)
    (license, trip)
  }*/

  def point(longitude: Double, latitude: Double): Point = {
    new Point(longitude, latitude)
  }

  def point(longitude: String, latitude: String): Point = {
    new Point(longitude.toDouble, latitude.toDouble)
  }

  def hasZero(trip: Trip): Boolean = {
    val zero = new Point(0.0, 0.0)
    zero.equals(trip.pickupLocation) || zero.equals(trip.dropoffLocation)
  }
}
