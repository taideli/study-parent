#!/bin/bash

CLUSTER_NAME=pseudo
INSTALL_HOME=/${CLUSTER_NAME}
DATA_HOME=/${CLUSTER_NAME}/data
LOG_HOME=/var/log/${CLUSTER_NAME}
CUSTOM_PROFILE=/etc/profile.d/${CLUSTER_NAME}.sh
INSTALL_LOG=${LOG_HOME}/install.log

function extract_name() {
    name=`tar -tf $1 2>/dev/null | head -n 1 | sed 's/\/$//'`
    echo ${name}
}

function logger() {
    echo "[`date +'%Y-%m-%d %H:%M:%S'`]: $@" >> ${INSTALL_LOG}
}