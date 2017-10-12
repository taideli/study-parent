package com.tdl.study.core.test.conf;


import com.tdl.study.core.conf.Configure;
import com.tdl.study.core.conf.ConfigureKey;

@Configure(value = "bate.properties", prefix = "bate")
public class BateConfigure {

    @ConfigureKey(value = "gender", type = String.class)
    private String gender;
}
