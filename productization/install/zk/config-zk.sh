#!/bin/bash

zk_conf_dir=${ZK_HOME}/conf

cp ${zk_conf_dir}/zoo-simple.cfg ${zk_conf_dir}/zoo.cfg
data_dir=${BG_DATA_HOME}/zookeeper/data
mkdir -p ${data_dir}
echo "1" > ${data_dir}/myid  # fix me, each slave has a myid file with different value
data_log_dir=${BG_LOG_HOME}/zookeeper/datalog

sed -i "s/^dataDir=*/dataDir=${data_dir}" ${zk_conf_dir}/zoo.cfg
sed -i "/^dataDir=/a\\dataLogDir=${data_log_dir}" ${zk_conf_dir}/zoo.cfg
host_id=1
for host in `echo ${zookeeper_hosts} | sed 's/,/ /g'`; do
    echo "server.${host_id}=${host}:2888:38888" >> ${zk_conf_dir}/zoo.cfg
    ((host_id++))
done
unset host_id
sed -i "s/^zookeeper.log.dir=*/zookeeper.log.dir=${BG_LOG_HOME}/zookeeper" ${zk_conf_dir}/log4j.properties
sed -i "s/^zookeeper.tracelog.dir=*/zookeeper.tracelog.dir=${BG_LOG_HOME}/zookeeper/tracelog" ${zk_conf_dir}/log4j.properties
