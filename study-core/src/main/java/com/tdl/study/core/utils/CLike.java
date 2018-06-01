package com.tdl.study.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CLike {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    public static String __FILE__() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return elements[2].getFileName();
    }

    public static int __LINE__() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return elements[2].getLineNumber();
    }


    public static String __FUNC__() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return elements[2].getMethodName();
    }

    public static String __TIME__() {
        synchronized (sdf) {
            return sdf.format(new Date());
        }
    }

    public static String __FILE_FUNC_LINE_TIME__() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        sb.append(e.getFileName()).append(" ")
          .append(e.getMethodName()).append(" ")
          .append(e.getLineNumber()).append(" ")
          .append(__TIME__());
        return sb.toString();
    }
}
