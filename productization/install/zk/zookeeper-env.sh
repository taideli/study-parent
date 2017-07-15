#!/bin/bash

zlg=`egrep "^[[:space:]]*dataLogDir" ${ZOOCFGDIR}/zoo.cfg | sed -e 's/.*=//'`
if [ $? -eq 0 ]; then ZOO_LOG_DIR=${zlg}; fi
