#!/bin/bash

nohup zkServer.sh start &

# zkserver.sh status 报错，且没有org.apache.zookeeper.server.quorum.QuorumPeerMain为停止，否则为failed...