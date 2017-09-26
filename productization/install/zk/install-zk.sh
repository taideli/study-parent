#!/bin/bash

ZK_DIR=`extract_name ${zookeeper}`
tar zxf ${zookeeper} -C ${INSTALL_HOME}
ZH_HOME=${INSTALL_HOME}/zookeeper
mv ${INSTALL_HOME}/${ZK_DIR} ${ZH_HOME}

echo "

# zookeeper settings
export ZK_HOME=${INSTALL_HOME}/${zk_dir_name}
export PATH=\$PATH:\$ZK_HOME/bin
# settings for spark on yarn
" >> ${CUSTOM_PROFILE}

# config zookeeper
ZK_CONF_DIR=${ZK_HOME}/conf

mv ${ZK_CONF_DIR}/zoo-simple.cfg ${ZK_CONF_DIR}/zoo.cfg
ZK_DATA_DIR=${DATA_HOME}/zookeeper/data
mkdir -p ${ZK_DATA_DIR}
echo "1" > ${ZK_DATA_DIR}/myid  # TODO fix me, each slave has a myid file with different value
ZK_DATA_LOG_DIR=${LOG_HOME}/zookeeper/datalog
# 在conf 目录生成zookeeper-env.sh 内容是
# #!/bin/bash
# export ZOO_LOG_DIR=....

sed -i "s/^dataDir=*/dataDir=${ZK_DATA_DIR}" ${ZK_CONF_DIR}/zoo.cfg
sed -i "/^dataDir=/a\\dataLogDir=${ZK_DATA_LOG_DIR}" ${ZK_CONF_DIR}/zoo.cfg
host_id=1
for host in `echo ${zookeeper_hosts} | sed 's/,/ /g'`; do
    echo "server.${host_id}=${host}:2888:38888" >> ${ZK_CONF_DIR}/zoo.cfg
    ((host_id++))
done
sed -i "s/^zookeeper.log.dir=*/zookeeper.log.dir=${LOG_HOME}/zookeeper" ${ZK_CONF_DIR}/log4j.properties
sed -i "s/^zookeeper.tracelog.dir=*/zookeeper.tracelog.dir=${LOG_HOME}/zookeeper/trace" ${ZK_CONF_DIR}/log4j.properties
