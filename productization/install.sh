#!/bin/bash

# touch profile.d/taidl.sh
custom_profile=/etc/profile.d/taidl.sh
if [ ! -f ${custom_profile} ]; then
    cp ./common.sh /etc/profile.d/taidl.sh
fi

. packages/componments.properties
#check_if_componments_ready


. ./install/java/install-jdk.sh
