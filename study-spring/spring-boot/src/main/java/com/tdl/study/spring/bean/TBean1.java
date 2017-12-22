package com.tdl.study.spring.bean;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TBean1 implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        System.out.println("TBean1 run args: " + Stream.of(args).collect(Collectors.toList()));
    }
}
