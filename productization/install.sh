#!/bin/bash

. ./common.sh

if [ ! -f ${CUSTOM_PROFILE} ]; then echo "#!/bin/bash" >> ${CUSTOM_PROFILE}; fi
if [ ! -d ${INSTALL_HOME} ]; then mkdir -p ${INSTALL_HOME}; fi
if [ ! -d `dirname ${INSTALL_LOG}` ]; then mkdir -p `dirname ${INSTALL_LOG}`; fi

. ./componments.properties

. ./install/java/install-jdk.sh
