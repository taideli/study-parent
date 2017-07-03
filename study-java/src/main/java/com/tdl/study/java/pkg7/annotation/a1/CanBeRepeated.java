package com.tdl.study.java.pkg7.annotation.a1;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Repeatable(RepeatedValue.class)
public @interface CanBeRepeated {
    String value();
}
