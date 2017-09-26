#!/bin/bash

JDK_DIR=`extract_name ${jdk}`
logger "install jdk..."
tar zxf ${jdk} -C ${INSTALL_HOME}
JAVA_HOME=${INSTALL_HOME}/${JDK_DIR}
logger "JAVA_HOME is $JAVA_HOME"
echo "

# jdk settings
export JAVA_HOME=${JAVA_HOME}
export PATH=\$PATH:\$JAVA_HOME/bin
" >> ${CUSTOM_PROFILE}

logger "jdk install finished"

