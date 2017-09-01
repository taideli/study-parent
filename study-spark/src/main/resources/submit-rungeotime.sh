#!/bin/bash

$SPARK_HOME/bin/spark-submit \
--class com.tdl.study.spark.nk.RunGeoTime \
--master local \
--jars /taidl/lib/esri-geometry-api-1.2.1.jar,/taidl/lib/nscala-time_2.11-2.16.0.jar,/taidl/lib/joda-convert-1.2.jar,/taidl/lib/joda-time-2.9.7.jar,/taidl/lib/spray-json_2.11-1.3.3.jar,/taidl/lib/json-20090211.jar \
--verbose \
--deploy-mode client \
--conf "spark.app.name=NewYorkTripDataAnalysis" \
/taidl/lib/study-spark-1.0.jar $*

