#!/bin/bash

function print_status() {
  if [ $# -ne 2 ]; then return 1; fi
  s=$1; ss=$2; _PSSL=24; _PSSSL=8
  if [ "$ss"x = "Running"x ]; then
    printf " service \033[1m%-${_PSSL}s\033[0m \033[1;32m[ %-${_PSSSL}s]\033[0m\n" "$s" "$ss"
  elif [ "$ss"x = "Stopped"x ]; then
    printf " service \033[1m%-${_PSSL}s\033[0m \033[1;31m[ %-${_PSSSL}s]\033[0m\n" "$s" "$ss"
  elif [ "$ss"x = "Failed"x ]; then
    printf " service \033[1m%-${_PSSL}s\033[0m \033[1;5;31m[ %-${_PSSSL}s]\033[0m\n" "$s" "$ss"
  else
    printf " service \033[1m%-${_PSSL}s\033[0m \033[1;33m[ %-${_PSSSL}s]\033[0m\n" "$s" "Unknown"
  fi
}

#print_status "namenode" "Running"
#print_status "datanode" "Stopped"
#print_status "zkServer abcd" "Failed"
#print_status "rdsfsdfsdf abcd" "FailedDFASF"
