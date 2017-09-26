#!/bin/bash

. ./common.sh

if [ ! -f ${CUSTOM_PROFILE} ]; then echo "#!/bin/bash" >> ${CUSTOM_PROFILE}; fi
if [ ! -d ${INSTALL_HOME} ]; then mkdir ${INSTALL_HOME}; fi

. ./componments.properties

. ./install/java/install-jdk.sh
