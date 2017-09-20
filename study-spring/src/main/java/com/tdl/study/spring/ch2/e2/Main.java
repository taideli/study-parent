package com.tdl.study.spring.ch2.e2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ResourceConfig.class);
        ElConfig elConfig = context.getBean(ElConfig.class);

        elConfig.outputResource();
        context.close();
    }
}
