package com.tdl.study.spring.ch1.e3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/* 6. 运行类 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
        DemoAnnotationService annotationService = context.getBean(DemoAnnotationService.class);
        DemoMethodService methodService = context.getBean(DemoMethodService.class);

        annotationService.add();
        methodService.add();
        context.close();
    }
}
