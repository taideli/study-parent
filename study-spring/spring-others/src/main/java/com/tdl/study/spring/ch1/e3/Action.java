package com.tdl.study.spring.ch1.e3;

import java.lang.annotation.*;

/* 1. 拦截规则的注解 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
    String name();
}
