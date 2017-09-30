package com.tdl.study.core.test.conf;

import com.tdl.study.core.conf.Configs;

import static com.tdl.study.core.conf.Configs.Config;

@Config(value = "test.properties", prefix = "conf.test")
public class Test {
    public static void main(String[] args) {
        Configs.map().forEach((k, v) -> System.out.println(k + " <====> " + v));
    }
}
