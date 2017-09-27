#!/bin/bash

#!/bin/bash

REPLICA_COUNT=3

ZK_DIR=`extract_name ${zookeeper}`
tar zxf ${zookeeper} -C ${INSTALL_HOME}
ZK_HOME=${INSTALL_HOME}/zookeeper
if [ ! -d ${ZK_HOME} ]; then mkdir -p ${ZK_HOME}; fi
for idx in `seq 1 ${REPLICA_COUNT}`; do
  SRV_HOME=${ZK_HOME}/server_${idx}
  cp -r ${INSTALL_HOME}/${ZK_DIR} ${SRV_HOME}

  # config zookeeper
  ZK_CONF_DIR=${SRV_HOME}/conf
  cp ./install/zk/zookeeper-env.sh ${ZK_CONF_DIR}
  mv ${ZK_CONF_DIR}/zoo_sample.cfg ${ZK_CONF_DIR}/zoo.cfg
  ZK_DATA_DIR=${DATA_HOME}/zookeeper/data_${idx}
  mkdir -p ${ZK_DATA_DIR}
  echo "${idx}" > ${ZK_DATA_DIR}/myid

  DATA_LOG_DIR=${LOG_HOME}/zookeeper/server_${idx}/datalog
  # 在conf 目录生成zookeeper-env.sh 内容是
  # #!/bin/bash
  # export ZOO_LOG_DIR=....

  # USE ANOTHER separator instead of '/' for 'sed'
  sed -i "s|^dataDir=.*$|dataDir=${ZK_DATA_DIR}|" ${ZK_CONF_DIR}/zoo.cfg
  sed -i "/^dataDir=/a\\dataLogDir=${DATA_LOG_DIR}" ${ZK_CONF_DIR}/zoo.cfg
  sed -i "s|^clientPort=.*$|clientPort=218${idx}|" ${ZK_CONF_DIR}/zoo.cfg
  for id in `seq 1 ${REPLICA_COUNT}`; do
      echo "server.${id}=0.0.0.0:288${id}:388${id}" >> ${ZK_CONF_DIR}/zoo.cfg
  done
  sed -i "s|^zookeeper.log.dir=.*$|zookeeper.log.dir=${LOG_HOME}/zookeeper/server_${idx}|" ${ZK_CONF_DIR}/log4j.properties
  sed -i "s|^zookeeper.tracelog.dir=.*$|zookeeper.tracelog.dir=${LOG_HOME}/zookeeper/server_${idx}|" ${ZK_CONF_DIR}/log4j.properties

done

rm -rf ${INSTALL_HOME}/${ZK_DIR}
echo "

# zookeeper settings
export ZK_HOME=${INSTALL_HOME}/${zk_dir_name}
export PATH=\$PATH:\$ZK_HOME/bin
# settings for spark on yarn
" >> ${CUSTOM_PROFILE}


