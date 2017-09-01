package com.tdl.study.spark.nk

import com.esri.core.geometry.Point
import org.joda.time.DateTime

case class Trip(
                 pickupTime: DateTime,
                 dropoffTime: DateTime,
                 pickupLocation: Point,
                 dropoffLocation: Point) {
  override def toString: String =
    String.format("%s %s %s %s", pickupTime.toString, pickupLocation.toString, dropoffTime.toString, dropoffLocation.toString)
}
