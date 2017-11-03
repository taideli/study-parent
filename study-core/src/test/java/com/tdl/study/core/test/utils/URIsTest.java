package com.tdl.study.core.test.utils;

import com.tdl.study.core.utils.URIs;

import java.io.UnsupportedEncodingException;

public class URIsTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        URIs uri = URIs.builder()
                .schema("elastic search")
//                .username("jdbcd%")
                .password("!@DWE%$#dfg543")
                .host("172.16.16.232", null)
                .host("172.16.16.233", 1234)
                .parameter("name", null)
                .parameter("age", 18)
                .parameter("姓名", "习大大")
                .path("p/ath//aa/bb///dd")
                .path("t?o")
                .path("your")
                .path(null)
                .path("dest")
                .fragment("DESCRIPTION")
                .build();

        System.out.println("[" + uri.getUsername() + "]");
        System.out.println("[" + uri.getPassword() + "]");

        System.out.println(uri);
        System.out.println(uri.toString(true));
        System.out.println(uri.toString(false));
    }
}
