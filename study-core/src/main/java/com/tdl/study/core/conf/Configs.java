package com.tdl.study.core.conf;

import com.tdl.study.core.utils.IOs;
import com.tdl.study.core.utils.Systems;
import com.tdl.study.core.utils.Texts;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class Configs {

    private static final Conf config = init();
    private static final String SYSTEM_CONFIG_FILE_EXTENSION = "com.tdl.study.core.conf.extension";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Config {
        String value() default "";

        String prefix() default "";
    }

    public static Conf init() {
        return init(Systems.getMainClass());
    }

    public static Conf init(Class<?> clazz) {
        Config config = clazz.getAnnotation(Config.class);
//        clazz.getAnnotations()
        return null != config ? init(clazz, config.value(), config.prefix()) : null;
    }

    private static Conf init(Class<?> clazz, String filename, String prefix) {
        Map<String, String> settings = new HashMap<>();

        /** 1. 加载系统属性 */
        System.getProperties().forEach((k, v) -> settings.put(k.toString(), v.toString()));
        /** 2. 加载配置文件属性*/
        String extension = defaultConfFileExtension();
        if (!filename.endsWith(extension)) filename = filename + extension;
        // 优先级 -cp下 classpath下 当前目录下  要去重


        try (InputStream is = IOs.open(filename)) {
            fill(settings, null, is);
        } catch (IOException ignored) {}
        // TODO: 2017/9/26 where to find the file
//        throw new RuntimeException("Code not completed.");
//        System.out.println("====filename:" + filename);
//        String file = "";
        return new Conf(prefix, settings);
    }

    /**
     * 优先级 -cp下 > jar中 > 当前目录下
     * @param clazz
     * @param filename
     * @return
     */
    private static List<String> findFiles(Class<?> clazz, String filename) {
        List<String> files = new ArrayList<>();

        return files;
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

    public static String get(String priority, String key) {
        return get(priority, key, (String[]) null);
    }

    public static String get(String key, String... defaults) {
        return get(null, key, defaults);
    }

    public static String getd(String key, String... defaults) {
        return get(null, key, defaults);
    }

    public static String getp(String priority, String key) {
        return get(priority, key, (String[]) null);
    }

    public static String get(String priority, String key, String... defaults) {
        return config.get(priority, key, defaults);
    }

    public static final class Conf {
        protected final Path file;
        private final String prefix;
        private final Map<String, String> configs;

        public Conf(String prefix, Map<String, String> configs) {
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

        public String get(String key) {
            return get(key, (String[]) null);
        }

        public String get(String priority, String key) {
            return get(priority, key, (String[]) null);
        }

        public String get(String key, String... defaults) {
            return get(null, key, defaults);
        }

        public String getd(String key, String... defaults) {
            return get(null, key, defaults);
        }

        public String getp(String priority, String key) {
            return get(priority, key, (String[]) null);
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

        public boolean has(String key) {
            return configs.containsKey(prefixedKey(key));
        }
    }
}
