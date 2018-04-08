package com.tdl.study.java.test;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

    @Test
    public void t0() throws UnknownHostException {
        //得到的是主机名
        System.out.println(InetAddress.getByName("172.16.16.232").getCanonicalHostName());
        // 得到的是主机别名
        System.out.println(InetAddress.getByName("172.16.16.232").getHostName());
        // 一般情况下得到的是一样的
    }

    @Test
    public void t1() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println(address.getHostName());
        System.out.println(address.getHostAddress());
    }
}
