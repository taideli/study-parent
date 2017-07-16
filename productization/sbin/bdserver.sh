#!/bin/bash


function bdserver_start_all() {
  echo "start all"
}

function bdserver_stop_all() {
  echo "stop all"
}

function bdserver_restart_all() {
  echo "restart all"
}

function bdserver_status_all() {
  echo "status_all"
}

function bdserver_usage() {
  cat << EOF
$0 is a shell cluster manager, your can use it to manage your cluster.
Usage: $0 <CMD> [SERVICE]
    CMD    : start stop restart status
    SERVICE: zookeeper namenode datanode

EOF
}

CMD=""; SRV=""
if [ $# -eq 0 ]; then bdserver_usage; exit 0
elif [ $# -eq 1 ]; then CMD=$1; SRV="all"
elif [ $# -eq 2 ]; then CMD=$1; SRV=$2; fi
case ${CMD} in 'start');; 'stop');; 'restart');; 'status');; *) echo "unknown cmd: $CMD"; exit 1;; esac
case ${SRV} in 'zookeeper');; 'datanode');; 'namenode');; 'all');; *) echo "unknown service: $SRV"; exit 1;; esac
_CMD=bdserver_${CMD}_${SRV}; ${_CMD}



