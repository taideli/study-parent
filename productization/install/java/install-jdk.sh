#!/bin/bash

jdk_short_name=`extract_name ${jdk}`
tar zxf ${jdk} -C ${INSTALL_HOME}

echo "

# jdk settings
export JAVA_HOME=${INSTALL_HOME}/${jdk_short_name}
export PATH=\$PATH:\$JAVA_HOME/bin
" >> ${CUSTOM_PROFILE}
unset jdk_short_name

