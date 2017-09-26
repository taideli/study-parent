package com.tdl.study.core.utils;

import java.io.FileInputStream;
import java.io.InputStream;

public class IOs {

    public static InputStream open(String file) {
        if (null == file || 0 == file.length()) return null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (Exception ignored) {}
        return is;
    }

    public static InputStream openInClasspath(Class<?> clazz, String file) {
        if (null == clazz || null == file || 0 == file.length()) return null;
        return clazz.getResourceAsStream(file);
    }

    public static InputStream openInClasspath(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }
}
