package com.tdl.study.core.conf;

import com.tdl.study.core.log.Logger;
import com.tdl.study.core.utils.IOs;
import com.tdl.study.core.utils.Strings;
import com.tdl.study.core.utils.Systems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Configs {

    private static final Pattern pattern = Pattern.compile("(?<key>^[a-zA-z0-9]+([.][a-zA-Z0-9]+)*)=(?<value>[^\\s]+)(\\s+[#]\\s*(?<ann>.*))?");
    private static final Logger log = Logger.getLogger(Configs.class);
//    private static final Conf config = init();
    private static ConcurrentHashMap<String, String> instance;
    private static String prefix;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Config {
        /**
         * the name of the config file
         */
        String value() default "";

        /**
         * prefix of valid config fields
         */
        String prefix() default "";

        boolean ignoreSystemFields() default false;
    }

    private static void init() {
        init(Systems.getMainClass());
    }

    private static void init(Class<?> clazz) {
        Config config = clazz.getAnnotation(Config.class);
        if (null != config) init(clazz, config.value(), config.prefix(), config.ignoreSystemFields());
    }

    private static void init(Class<?> clazz, String filename, String prefix, boolean ignoreSystemFields) {
        Configs.prefix = prefix.replaceAll("\\.$", "");
        instance = new ConcurrentHashMap<>();
        /** 1. 加载系统属性 */
        fill(instance, key -> (systemPrefix(key) && ignoreSystemFields) || validPrefix(Configs.prefix, key), mapProp(System.getProperties()));
        /** 2. 加载配置文件属性*/
//        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        String extension = ".properties";
        if (!filename.endsWith(extension)) filename = filename + extension;
        // 优先级 先从classPath下找，找不到则从jar包中找 再找不到从当前目录找
        try (InputStream cis = IOs.openInClasspath(clazz, filename)) {
            if (!fill(instance, null, cis)) try (InputStream dis = IOs.openInCurrentDir(filename)) {
                if (!fill(instance, null, dis)) throw new FileNotFoundException(filename);
                else log.info("load from " + Paths.get("").toAbsolutePath().toString());
            } else log.info("load from classpath");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean systemPrefix(String key) {
        List<String> systemKeyPrefix = Arrays.asList("sun", "awt", "java", "os", "file", "user", "path", "line");
        for (String prefix : systemKeyPrefix) {
            if (key.startsWith(prefix)) return true;
        }
        return false;
    }

    private static boolean validPrefix(String prefix, String key) {
        return key.startsWith(prefix);
    }

    private static Map<String, String> mapProp(Properties properties) {
        Map<String, String> map = new HashMap<>();
        properties.forEach((key, value) -> map.put(key.toString(), value.toString()));
        return map;
    }

    private static boolean fill(Map<String, String> org, Predicate<String> filter, InputStream is) {
        if (null == is) return false;
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            return false;
        }
        return fill(org, filter, mapProp(p));
    }

    private static boolean fill(Map<String, String> org, Predicate<String> filter, Map<String, String> kvs) {
        if (null == kvs || 0 == kvs.size()) return false;
        for (String key : kvs.keySet()) {
            String value = kvs.get(key);
            if (null == value) continue;  // ignore key with empty value
            if (null == filter || !filter.test(key)) org.put(key, value);// will cover entry that already exist
        }
        return true;
    }

    public static Map<String, String> map() {
        if (null == instance) init();
        return new ConcurrentHashMap<>(instance);
    }

    public static boolean containsKey(String key) {
        if (null == instance) init();
        return instance.containsKey(prefixedKey(key));
    }

    public static boolean contains(String value) {
        if (null == instance) init();
        return instance.contains(value);
    }

    public static String get(String key, String... defaults) {
        if (null == instance) init();
        String value = instance.get(prefixedKey(key));
        return null == value ? defaults[0] : value;
    }

    private static String prefixedKey(String key) {
        return Strings.isEmpty(prefix) ? key : prefix + "." + key;
    }

    private static InputStream fetchConfigFileInputStream(Class<?> clazz, String filename) {
        // 1. 从 classpath中找

        return null;
    }

}
