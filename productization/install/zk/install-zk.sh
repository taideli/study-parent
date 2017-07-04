#!/bin/bash

set zk_org_short_name=`tar -tf ${zk} | head -n 1 | sed 's/.$//'`
tar zxf ${zk} -C ${BG_INSTALL_HOME}
set zk_short_name=hadoop
mv ${BG_INSTALL_HOME}/${zk_org_short_name} ${BG_INSTALL_HOME}/${zk_short_name}

echo "\
# zookeeper settings
export ZK_HOME=${BG_INSTALL_HOME}/${hadoop_short_name}
export PATH=\$PATH:\$ZK_HOME/bin:\$ZK_HOME/sbin
# settings for spark on yarn
" >> ${custom_profile}

unset zk_org_short_name
unset zk_short_name
source /etc/profile

. ./config-zk.sh