package com.tdl.study.java.pkg7.annotation.a1;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface RepeatedValue {
    CanBeRepeated[] value();
}

