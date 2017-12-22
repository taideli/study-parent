package com.tdl.study.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArgsBean {

    @Autowired
    public ArgsBean(ApplicationArguments args) {
        boolean debug = args.containsOption("debug");
        List<String> argss = args.getNonOptionArgs();

        System.out.println("argss========================" + argss);
    }
}
