package com.tdl.study.core.conf;

import com.tdl.study.core.utils.IOs;
import com.tdl.study.core.utils.Systems;
import com.tdl.study.core.utils.Texts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Configs {

//    private static final Logger log = Logger.getLogger(Configs.class);
    private static final Conf config = init();
    private static final String SYSTEM_CONFIG_FILE_EXTENSION = "com.tdl.study.core.conf.extension";

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

        boolean ignoreInvalidFields() default false;

        boolean ignoreSystemFields() default false;
    }

    private static Conf init() {
        return init(Systems.getMainClass());
    }

    private static Conf init(Class<?> clazz) {
        Config config = clazz.getAnnotation(Config.class);
        return null != config ? init(clazz, config.value(), config.prefix()) : null;
    }

    private static Conf init(Class<?> clazz, String filename, String prefix) {
        Map<String, String> settings = new HashMap<>();

        /** 1. 加载系统属性 */
        fill(settings, Configs::systemPrefix, mapProp(System.getProperties()));
        /** 2. 加载配置文件属性*/
//        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        String extension = defaultConfFileExtension();
        if (!filename.endsWith(extension)) filename = filename + extension;
        // 优先级 先从classPath下找，找不到则从jar包中找 再找不到从当前目录找
        try (InputStream cis = IOs.openInClasspath(clazz, filename)) {
            if (!fill(settings, null, cis)) try (InputStream dis = IOs.openInCurrentDir(filename)) {
                if (!fill(settings, null, dis)) throw new FileNotFoundException(filename);
                else System.out.println("load from " + Paths.get("").toAbsolutePath().toString());
            } else System.out.println("load from classpath");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Conf(prefix, settings);
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

    private static String defaultConfFileExtension() {
        return "." + System.getProperty(SYSTEM_CONFIG_FILE_EXTENSION, "properties").replaceFirst("^\\.", "");
    }

    public static Map<String, String> map() {
        return config.map();
    }

    public static boolean has(String key) {
        return config.has(key);
    }

    public static String get(String key) {
        return get(key, (String[]) null);
    }

    public static String get(String key, String... defaults) {
        return get(null, key, defaults);
    }

    public static String getp(String priority, String key) {
        return get(priority, key, (String[]) null);
    }

    private static String get(String priority, String key, String... defaults) {
        return config.get(priority, key, defaults);
    }

    public static final class Conf {
        private final Path file;
        private final String prefix;
        private final Map<String, String> configs;

        Conf(String prefix, Map<String, String> configs) {
            if (null == prefix || prefix.length() == 0) this.prefix = "";
            else {
                if (!prefix.endsWith(".")) prefix = prefix + ".";
                this.prefix = prefix;
            }
            this.file = null;
            this.configs = configs;
        }

        String prefixedKey(String key) {
            return prefix + key;
        }

        public String get(String priority, String key, String... defaults) {
            return Texts.isEmpty(priority) ? configs.getOrDefault(prefixedKey(key), first(defaults)) : priority;
        }

        public Map<String, String> map() {
            return new HashMap<>(configs);
        }

        private static String first(String... defaults) {
            return null == defaults || defaults.length == 0 ? null : defaults[0];
        }

        boolean has(String key) {
            return configs.containsKey(prefixedKey(key));
        }
    }
}
