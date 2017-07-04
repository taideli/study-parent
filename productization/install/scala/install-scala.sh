#!/bin/bash

set scala_short_name=`tar -tf ${scala} | head -n 1 | sed 's/\/$//'`
tar zxf ${scala} -C ${BG_INSTALL_HOME}

echo "\
# scala settings
export SCALA_HOME=${BG_INSTALL_HOME}/${scala_short_name}
export PATH=\$PATH:\$SCALA_HOME/bin
" >> ${custom_profile}

unset scala_short_name
source /etc/profile
