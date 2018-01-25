package com.tdl.study.core.utils;

public class Systems {

    public static Class<?> getMainClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        }
        catch (ClassNotFoundException ignored) {}
        throw new RuntimeException("can NOT get main class.");
    }

    public static void main(String[] args) {
        System.out.println("main class=" + Systems.getMainClass());
    }
}
