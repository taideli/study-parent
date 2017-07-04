#!/bin/bash

set hadoop_org_short_name=`tar -tf ${hadoop} | head -n 1 | sed 's/.$//'`
tar zxf ${hadoop} -C ${BG_INSTALL_HOME}
set hadoop_short_name=hadoop
mv ${BG_INSTALL_HOME}/${hadoop_org_short_name} ${BG_INSTALL_HOME}/${hadoop_short_name}

echo "\
# hadoop settings
export HADOOP_HOME=${BG_INSTALL_HOME}/${hadoop_short_name}
export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin
# settings for spark on yarn
export HADOOP_CONF_DIR=\${HADOOP_HOME}/etc/hadoop
" >> ${custom_profile}

unset hadoop_org_short_name
unset hadoop_short_name
source /etc/profile

. ./config-hadoop.sh