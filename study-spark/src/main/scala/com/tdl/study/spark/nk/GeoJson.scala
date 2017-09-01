package com.tdl.study.spark.nk

import com.esri.core.geometry.{Geometry, GeometryEngine}
import spray.json._


case class Feature(
  id: Option[JsValue],
  properties: Map[String, JsValue],
  geometry: RichGeometry) {

   def apply(property: String): JsValue = properties(property)

  def get(property: String): Option[JsValue] = properties.get(property)
}

case class FeatureCollection(features: Array[Feature]) extends IndexedSeq[Feature] {
  override def length: Int = features.length

  override def apply(idx: Int): Feature = features(idx)
}

case class GeometryCollection(geometries: Array[RichGeometry]) extends IndexedSeq[RichGeometry] {
  override def length: Int = geometries.length

  override def apply(idx: Int): RichGeometry = geometries(idx)
}

object GeoJsonProtocol extends DefaultJsonProtocol {
  implicit object RichGeometryJsonFormat extends RootJsonFormat[RichGeometry] {
    override def write(g: RichGeometry): JsValue = {
      val json = GeometryEngine.geometryToGeoJson(g.csr, g.geometry)
      JsString(json)
    }

    override def read(value: JsValue): RichGeometry = {
      val mg = GeometryEngine.geometryFromGeoJson(value.compactPrint, 0, Geometry.Type.Unknown)
      new RichGeometry(mg.getGeometry, mg.getSpatialReference)
    }
  }

  implicit object FeatureJsonFormat extends RootJsonFormat[Feature] {
    override def write(f: Feature): JsObject = {
      val buf = scala.collection.mutable.ArrayBuffer(
        "type" -> JsString("Feature"),
        "properties" -> JsObject(f.properties),
        "geometry" -> f.geometry.toJson)
      f.id.foreach(v => {buf += "id" -> v})
      JsObject(buf.toMap)
    }

    override def read(value: JsValue): Feature = {
      val jso = value.asJsObject
      val id = jso.fields.get("id")
      val properties = jso.fields("properties").asJsObject.fields
      val geometry = jso.fields("geometry").convertTo[RichGeometry]
      Feature(id, properties, geometry)
    }
  }

  implicit object FeatureCollectionJsonFormat extends RootJsonFormat[FeatureCollection] {
    override def write(fc: FeatureCollection): JsObject = {
      JsObject(
        "type" -> JsString("FeatureCollection"),
        "features" -> JsArray(fc.features.map(_.toJson): _*)
      )
    }

    override def read(value: JsValue): FeatureCollection = {
      FeatureCollection(value.asJsObject.fields("features").convertTo[Array[Feature]])
    }
  }

  implicit object GeometryCollectionJsonFormat extends RootJsonFormat[GeometryCollection] {
    def write(gc: GeometryCollection): JsObject = {
      JsObject(
        "type" -> JsString("GeometryCollection"),
        "geometries" -> JsArray(gc.geometries.map(_.toJson): _*))
    }

    def read(value: JsValue): GeometryCollection = {
      GeometryCollection(value.asJsObject.fields("geometries").convertTo[Array[RichGeometry]])
    }
  }
}