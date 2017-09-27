package com.tdl.study.core.utils;

public class Reflections {

    public static String packageName(Class<?> clazz) {
        return null == clazz || null == clazz.getPackage() ? "" : clazz.getPackage().getName();
    }
}
