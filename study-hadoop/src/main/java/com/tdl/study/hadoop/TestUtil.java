package com.tdl.study.hadoop;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TestUtil {

    public TestUtil() {}

    public static void main(String[] args) throws Exception {

        List<String> commands = Stream.of(System.getProperty("sun.java.command").split("\\s+")).collect(Collectors.toList());
        commands.remove(0); // org.apache.hadoop.util.RunJar
        commands.remove(0); // remove param Jar name
        String mainClassName = commands.remove(0); // really class name
        Class<?> clazz = Class.forName(mainClassName);
        if (!TestUtil.class.isAssignableFrom(clazz)) {
            System.out.println("MAIN_CLASS `" + clazz.getName() + "` is not a `" + TestUtil.class.getName() + "`");
            System.exit(1);
        }
        args = commands.toArray(new String[]{});
        Object test = clazz.newInstance();
        List<Method> methods = methods(test);
        if (0 == args.length) {
            System.out.println("run all public non-parameter & non-value-return functions, total: " + methods.size());
            for (Method method : methods) invoke(test, method);
        } else {
            for (String func : args) {
                Method method = methods.stream().filter(m -> m.getName().equals(func)).findFirst().orElse(null);
                if (null == method) {
                    System.out.println("no such method that meeting the name `" + func + "`");
                } else {
                    invoke(test, method);
                }
            }
        }
    }

    private static List<Method> methods(Object object) {
        if (null == object) return new ArrayList<>();
        return Stream.of(object.getClass().getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> method.getReturnType() == Void.TYPE)
                .collect(Collectors.toList());
    }

    private static void invoke(Object o, Method method) throws Exception {
        if (null == o || null == method) return;
        String m = method.getName();
        int rectX = 70;
        String stars = Stream.generate(() -> "*").limit(rectX).collect(Collectors.joining(""));
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(stars);
        sb.append("\n*");
        int whiteX = rectX - m.length() - 2;
        int frontWhiteX = whiteX / 2;
        sb.append(Stream.generate(() -> " ").limit(frontWhiteX).collect(Collectors.joining("")));
        sb.append(m);
        sb.append(Stream.generate(() -> " ").limit(whiteX - frontWhiteX).collect(Collectors.joining("")));
        sb.append("*\n");
        sb.append(stars);
        System.out.println(sb.toString());
        method.invoke(o);
    }
}
