package com.tdl.study.core.utils;

public class Systems {

    public static Class<?> getMainClass() {
        String name = System.getProperty("sun.java.command");
        if (null != name) try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {}
        return null;
    }

    public static void main(String[] args) {
        System.getProperties().forEach((o1, o2) -> {
            System.out.println(o1 + "<===>" + o2);
        });

        System.out.println("exec.mainClass=" + System.getProperty("exec.mainClass"));
    }
}
