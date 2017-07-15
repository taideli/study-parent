#!/bin/bash

function start_zks() {
  for host in `cat /etc/hosts | grep -v localhost | awk '{print $2}'`; do
    ssh $host /taidl/zookeeper/bin/zkServer.sh start;
  done
}

function status_zks() {
  for host in `cat /etc/hosts | grep -v localhost | awk '{print $2}'`; do
    ssh $host /taidl/zookeeper/bin/zkServer.sh status;
  done
}

function stop_zks() {
  for host in `cat /etc/hosts | grep -v localhost | awk '{print $2}'`; do
    ssh $host /taidl/zookeeper/bin/zkServer.sh stop;
  done
}