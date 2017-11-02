package com.tdl.study.java.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TT {
    public static void main(String[] args) throws UnknownHostException {
        //得到的是主机名
        System.out.println(InetAddress.getByName("172.16.16.232").getCanonicalHostName());
        // 得到的是主机别名
        System.out.println(InetAddress.getByName("172.16.16.232").getHostName());
        // 一般情况下得到的是一样的
    }
}
