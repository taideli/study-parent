package com.tdl.study.core.conf;

import com.tdl.study.core.utils.Systems;
import com.tdl.study.core.utils.Texts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

public class Configs {

    private static final Conf config = init();

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

        // TODO: 2017/9/26 where to find the file
        throw new RuntimeException("Code not completed.");
//        System.out.println("====filename:" + filename);
//        String file = "";
//        return new Conf(prefix, settings);
    }

    private static String defaultConfFileExtension() {
        return "." + System.getProperty("com.tdl.study.core.conf.extension", "properties");
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
