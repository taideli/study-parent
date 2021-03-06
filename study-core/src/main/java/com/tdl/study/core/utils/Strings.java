package com.tdl.study.core.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Strings {

    public static boolean isEmpty(String string) {
        return null == string || string.isEmpty();
    }

    public static String join(final String delimiter, String ... values) {
        return join(delimiter, Arrays.asList(values));
    }

    public static String join(final String delimiter, Collection<String> values) {
        if (null == values || values.size() == 0) return null;
        return values.stream().filter(s -> null != s && s.length() != 0).collect(Collectors.joining(delimiter));
    }

    public static boolean isNumeric(String string) {
        if (null == string || string.isEmpty()) return false;
        for (int idx = 0; idx < string.length(); idx++) {
            if (!Character.isDigit(string.charAt(idx))) return false;
        }
        return true;
    }
}
