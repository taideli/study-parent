package com.tdl.study.java.pkg4.classloader;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RunTest {
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Usage: " + RunTest.class.getName() + " <class> <key> [params...]");
            return;
        }

        String name = args[0];
        int key = Integer.parseInt(args[1]);

        runClass(name, key, Arrays.copyOfRange(args, 2, args.length));
    }

    private static void runClass(String name, int key, String args[]) {
        try {
            ClassLoader loader = new CryptoClassLoader(key);
            Class<?> c = loader.loadClass(name);
            Method m = c.getMethod("main", String[].class);
            m.invoke(null, (Object) args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
