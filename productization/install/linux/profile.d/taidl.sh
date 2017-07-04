#!/bin/bash

# copy this file to dir /etc/profile.d and then execute 'source /etc/profile'

# bigdata home settings
export BG_INSTALL_HOME=/taidl
export BG_DATA_HOME=/taidl/data

# jdk settings
export JAVA_HOME=${BG_INSTALL_HOME}/jdk1.8.0_45
export PATH=$PATH:${JAVA_HOME}/bin

# scala settings
export SCALA_HOME=${BG_INSTALL_HOME}/scala-2.11.8
export PATH=$PATH:${SCALA_HOME}/bin

# hadoop settings
export HADOOP_HOME=${BG_INSTALL_HOME}/hadoop
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
# settings for spark on yarn
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop

# hbase settings
export HBASE_HOME=${BG_INSTALL_HOME}/hbase
export PATH=$PATH:${HBASE_HOME}/bin

# spark settings
export SPARK_HOME=${BG_INSTALL_HOME}/spark
export PATH=$PATH:${SPARK_HOME}/bin

