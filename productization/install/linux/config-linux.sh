#!/bin/bash


timedatectl set-timezone Asia/Shanghai

hostnamectl set-hostname taidl02

ssh-keygen -t rsa
cd .ssh; cat id_rsa.pub >> authorized_keys

# add hosts to /etc/hosts


