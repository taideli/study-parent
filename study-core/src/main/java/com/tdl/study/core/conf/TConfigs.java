package com.tdl.study.core.conf;

import com.tdl.study.core.log.Logger;
import com.tdl.study.core.utils.Strings;
import com.tdl.study.core.utils.Systems;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class TConfigs {

    public static final String SYSTEM_PROPERTY_FILENAME = "tdl.config.filename";
    public static final String SYSTEM_PROPERTY_IGNORED = "tdl.config.ignored";
    public static final String CONFIG_FILE_PROFILE_KEY = "tdl.config.profile";
    public static final String CONFIG_FILE_IMPORT_KEY = "tdl.config.import";
    private static final Pattern pattern = Pattern.compile("(?<key>^[a-zA-z0-9]+([.][a-zA-Z0-9]+)*)=(?<value>[^\\s]+)(\\s+[#]\\s*(?<ann>.*))?");


    private static final Logger logger = Logger.getLogger(TConfigs.class);
    private static final boolean IGNORED_SYSTEM_PROPERTY = false;
    private static final Map<String, String> configs = init();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Config {
        String value() default "";
        boolean ignoreSystemFields() default IGNORED_SYSTEM_PROPERTY;
    }

    private static Map<String, String> init() {
        return init(Systems.getMainClass());
    }

    private static Map<String, String> init(Class<?> clazz) {
        Map<String, String> configs = new HashMap<>();
        String filename = null;
        boolean ignored = false;
        Config config = clazz.getAnnotation(Config.class);
        if (null != config) {
            filename = config.value();
            ignored = config.ignoreSystemFields();
        }
        String redirectFilename = System.getProperty(SYSTEM_PROPERTY_FILENAME);
        String ignoredStr = System.getProperty(SYSTEM_PROPERTY_IGNORED);
        boolean redirectIgnored = null == ignoredStr ? IGNORED_SYSTEM_PROPERTY : Boolean.valueOf(ignoredStr);
        if (null != redirectFilename) {
            filename = redirectFilename;
            ignored = redirectIgnored;
            logger.debug(TConfigs.class.getSimpleName() + " will use redirected config filename: " + filename + ".");
        }

        /* fill system properties */
        final boolean ignore = ignored;
        fill(configs, (k, v)-> !ignore && !configs.containsKey(k), mapProp(System.getProperties()));

        if (null == filename) {
            logger.info("config file is not set");
        }
        init(configs, clazz, filename, true);

        // TODO: 2018/1/8 replace param in keys

        return configs;
    }

    private static void init(Map<String, String> configs, Class<?> clazz, String filename, boolean redirect) {
        /* fill from properties file */
        if (Strings.isEmpty(filename)) return;
        InputStream is = fileInputStream(clazz, filename);
        Map<String, String> tmp = new HashMap<>();
        fill(tmp, (k, v) -> true, is);
        if (isProfile(tmp) && redirect) {
            String profile = profile(tmp);
            String profiled = profiled(filename, profile);
            logger.debug("find profile '" + profile + "', redirect config file to '" + profiled + "'");
            init(configs, clazz, profiled, false);
        } else {
            List<String> imports = new ArrayList<>();
            fill(configs, (k, v) -> {
                if (k.startsWith(CONFIG_FILE_IMPORT_KEY + ".")
                        && Strings.isNumeric(k.substring(CONFIG_FILE_IMPORT_KEY.length() + 1))) {
                    logger.debug("config key '" + k + "' assigned to import config file '" + v + "'.");
                    imports.add(v);
                    return false;
                }
                if (configs.containsKey(k)) logger.debug(k + " is replaced with " + v + " in config file '" + filename + "'");
                return true;
            }, tmp);
            imports.forEach(file -> init(configs, clazz, file, true));
        }
    }

    private static boolean isProfile(Map<String, String> map) {
        return null != map && 1 == map.size() && map.containsKey(CONFIG_FILE_PROFILE_KEY);
    }

    private static String profile(Map<String, String> map) {
        return map.get(CONFIG_FILE_PROFILE_KEY);
    }

    private static String profiled(String filename, String profile) {
        List<String> strings = new ArrayList<>(Arrays.asList(filename.split("\\.")));
        int idx = 0;
        if (strings.size() >= 2) idx = strings.size() - 2;
        strings.set(idx, strings.get(idx) + "-" +profile);
        return strings.stream().collect(Collectors.joining("."));
    }

    private static Map<String, String> mapProp(Properties properties) {
        Map<String, String> map = new HashMap<>();
        properties.forEach((key, value) -> map.put(key.toString(), value.toString()));
        return map;
    }

    private static void fill(Map<String, String> config, BiPredicate<String, String> filter, Map<String, String> source) {
        assert null != config;
        if (null == source || 0 ==source.size()) return;
        source.forEach((k, v) -> {
            if (filter.test(k, v)) config.put(k, v);
        });
    }

    private static InputStream fileInputStream(Class<?> clazz, String filename) {
        // from classpath
        InputStream is = clazz.getResourceAsStream("/" + filename);
        if (null != is) {
            logger.debug("load configs from '" + clazz.getResource("/" + filename) + "'");
            return is;
        }

        // from directory where main jar is in
        String dir = Paths.get(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent().toString();
        String path = null;
        if (Files.exists(Paths.get(dir + File.separator + filename))) {
            path = dir + File.separator + filename;
        } else if (Files.exists(Paths.get(Paths.get("").toAbsolutePath() + File.separator + filename))) {
        // from current directory
            path = Paths.get("").toAbsolutePath() + File.separator + filename;
        }
        if (null == path) {
            logger.error("no " + filename + " founded in classpath, main directory or current directory.");
            return null;
        }
        try {
            is = new FileInputStream(path);
            logger.debug("load configs from '" + path + "'");
        } catch (FileNotFoundException ignored) {/*will must exists*/}
        return is;
    }

    private static void fill(Map<String, String> config, BiPredicate<String, String> filter, InputStream is) {
        assert null != config;
        if (null == is) return;
        stream2map(is).forEach((k, v) -> {
            if (filter.test(k, v)) config.put(k, v);
        });
    }

    private static Map<String, String> stream2map(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Map<String, String> configs = new HashMap<>();
        reader.lines().forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                configs.put(matcher.group("key"), matcher.group("value"));
            }
        });
        try { is.close();} catch (IOException ignored) {}
        return configs;
    }

    public static String get(String key, String... def) {
        String value = configs.get(key);
        return null == value /*&& null != def*/ && def.length > 0 ? def[0] : value;
    }

    @SafeVarargs
    public static <T> T get(String key, Class<? extends T> type, T... def) {
        String s = get(key);
        if (null != s)  { try {
            return type.getConstructor(String.class).newInstance(get(key));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("can not construct a " + type.getSimpleName() + " with param " + s);
        } } else {
            return null != def && def.length > 0 ? def[0] : null;
        }
    }

    public static Map<String, String> map() {
        return new HashMap<>(configs);
    }

}
