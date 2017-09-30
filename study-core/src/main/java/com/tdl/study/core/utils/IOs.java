package com.tdl.study.core.utils;

import java.io.FileInputStream;
import java.io.InputStream;

public class IOs {

    /**
     * open a file in same dir of mainClass or the Jar which mainClass in
     * @param file filename
     * @return Stream of the file, or null if not found
     */
    public static InputStream openInCurrentDir(String file) {
        if (null == file || 0 == file.length()) return null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (Exception ignored) {}
        return is;
    }

    /**
     * open the file find in classpath, if not exist, then find in jar file, or else do nothing
     * @param file filename
     * @return Stream of the file, or null if not found
     */
    public static InputStream openInClasspath(Class<?> clazz, String file) {
        if (null == clazz || null == file || 0 == file.length()) return null;
        return clazz.getResourceAsStream("/" + file.replaceFirst("^/", ""));
    }

    /**
     * open the resource find in same path of class in jar
     * @param resource filename
     * @return Stream of the resource, or null if not found
     */
    public static InputStream openInJarResources(Class<?> clazz, String resource) {
        if (null == clazz || null ==  resource || 0 == resource.length()) return null;
        return clazz.getResourceAsStream(resource.replaceFirst("^/", ""));
    }

    public static InputStream openInClasspath(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }
}
