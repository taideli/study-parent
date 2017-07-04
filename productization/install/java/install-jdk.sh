#!/bin/bash

set jdk_short_name=`tar -tf ${jdk} | head -n 1 | sed 's/\/$//'`
tar zxf ${jdk} -C ${BG_INSTALL_HOME}
echo "\
# jdk settings
export JAVA_HOME=${BG_INSTALL_HOME}/${jdk_short_name}
export PATH=\$PATH:\$JAVA_HOME/bin
" >> ${custom_profile}
unset jdk_short_name
source /etc/profile
