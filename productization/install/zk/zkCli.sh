#!/bin/bash

SCRIPT_BIN_DIR=`dirname $0`
ZK_SERVER_HOME=${SCRIPT_BIN_DIR}/..

CONFIG_DIRS=${ZK_SERVER_HOME}/server_*/conf
ZK_CNN=""
for port in `find ${CONFIG_DIRS} -name zoo.cfg | xargs egrep '^clientPort=[0-9]+[[:space:]]*$' | awk -F= '{print $NF}'`; do
  ZK_CNN="${ZK_CNN}127.0.0.1:${port},"
done
ZK_CNN=${ZK_CNN%,*}

if [ $# -eq 0 -o "$1" = '-h' -o "$1" = "--help" ]; then
  echo "Usage: `basename $0` <ZK_ID> [args...]"
  echo "Server: ${ZK_CNN}"
  exit 1
fi

ZK_DIR=${ZK_SERVER_HOME}/server_$1
if [ ! -d ${ZK_DIR} ]; then
  echo "${ZK_DIR} not exist."
  exit 1
fi

shift
${ZK_DIR}/bin/zkCli.sh "$@"