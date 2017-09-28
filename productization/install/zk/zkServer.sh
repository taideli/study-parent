#!/bin/bash

SCRIPT_BIN_DIR=`dirname $0`
ZK_SERVER_HOME=${SCRIPT_BIN_DIR}/..

#for server in `ls $ZK_SERVER_HOME | grep server_ | awk -F_ '{print $2}'`; do
#  echo "server: $server"
#done

CMD=$1
for SRV_DIR in  `ls ${ZK_SERVER_HOME} | grep server_`; do
  ${ZK_SERVER_HOME}/${SRV_DIR}/bin/zkServer.sh ${CMD}
#  echo "$CMD $SRV_DIR $?"
done
