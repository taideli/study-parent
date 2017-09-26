package com.tdl.study.core.test.conf;

import com.tdl.study.core.conf.Configs;

import static com.tdl.study.core.conf.Configs.Config;

@Config(value = "xxx.properties", prefix = "conf.test")
public class Test {
    public static void main(String[] args) {
        System.out.println(Configs.get("abc"));
        System.out.println("hello world");
    }
}
