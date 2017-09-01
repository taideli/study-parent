package com.tdl.study.spark.nk

import com.esri.core.geometry.{Geometry, GeometryEngine, SpatialReference}

class RichGeometry(
  val geometry: Geometry,
  val csr: SpatialReference = SpatialReference.create(4326)) extends Serializable {

  def area2D(): Double = geometry.calculateArea2D()

  def distance(other: Geometry): Double = {
    GeometryEngine.distance(geometry, other, csr)
  }

  def contains(other: Geometry): Boolean = {
    GeometryEngine.contains(geometry, other, csr)
  }

  def within(other: Geometry): Boolean = {
    GeometryEngine.within(geometry, other, csr)
  }

  def overlaps(other: Geometry): Boolean = {
    GeometryEngine.overlaps(geometry, other, csr)
  }

  def touches(other: Geometry): Boolean = {
    GeometryEngine.touches(geometry, other, csr)
  }

  def crosses(other: Geometry): Boolean = {
    GeometryEngine.crosses(geometry, other, csr)
  }

  def disjoint(other: Geometry): Boolean = {
    GeometryEngine.disjoint(geometry, other, csr)
  }
}

object RichGeometry extends Serializable {
  implicit def createRichGeometry(g: Geometry): RichGeometry = new RichGeometry(g)
}
