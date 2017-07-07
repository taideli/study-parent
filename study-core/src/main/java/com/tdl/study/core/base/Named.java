package com.tdl.study.core.base;

public interface Named {
    default String name() {return getClass().getSimpleName();}
}
