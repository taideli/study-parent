package com.tdl.study.core.test.conf;


import com.tdl.study.core.conf.Configure;
import com.tdl.study.core.conf.ConfigureKey;

@Configure(value = "alpha.properties", prefix = "alpha")
public class AlphaConfigure {

    @ConfigureKey(value = "name", type = String.class)
    private String name;
}
