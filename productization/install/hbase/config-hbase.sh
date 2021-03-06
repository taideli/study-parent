#!/bin/bash

hdfs_master="taidl01"
hdfs_port=9000
zk_quorum="taidl01,taidl02,taidl03"  # fix me....from zookeeper/conf/zoo.cfg

sed -i "/configuration/d" ${HBASE_HOME}/conf/hbase-site.xml
cat >> ${HBASE_HOME}/conf/hbase-site.xml <<EOF
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>hdfs://${hdfs_master}:${hdfs_port}/hbase</value>   <!--must be equal to fs.defaultFS of hadoop core-site.xml-->
  </property>

  <property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
  </property>

  <property>
    <name>hbase.zookeeper.quorum</name>
    <value>${zk_quorum}</value>
  </property>
</configuration>
EOF

sed -i "/HBASE_MANAGES_ZK/a\export HBASE_MANAGES_ZK=false" ${HBASE_HOME}/conf/hbase-env.sh
# export HBASE_LOG_DIR=/var/log/taidl/hbase/out

echo ${zk_quorum} | sed "s/,/\n/g" > ${HBASE_HOME}/conf/regionservers

sed -i "/hbase.log.dir=/hbase.log.dir=${BG_LOG_HOME}/hbase" ${HBASE_HOME}/conf/log4j.properties

# scp hbase-env.sh taidl03:`pwd`