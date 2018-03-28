package com.tdl.study.core.conf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RandomValueGenerator {
    public abstract String generate();

    public static RandomValueGenerator of(String expression) {
        return ExpressionAnalyzer.getGenerator(expression);
    }

    private static class ExpressionAnalyzer {
        private static final Pattern RANDOM_STRING_PATTERN  = Pattern.compile("random\\.string\\[(?<min>[0-9]+)(,(?<max>[0-9]+))?\\]");
        private static final Pattern RANDOM_INTEGER_PATTERN = Pattern.compile("random\\.integer\\[(?<min>[0-9]+),(?<max>[0-9]+)\\]");
        private static final Pattern RANDOM_LONG_PATTERN    = Pattern.compile("random\\.long\\[(?<min>[0-9]+),(?<max>[0-9]+)\\]");
        private static final Pattern RANDOM_DOUBLE_PATTERN = Pattern.compile("random\\.double(\\[(?<min>[0-9]{1,17}(\\.[0-9]{1,17})?),(?<max>[0-9]{1,17}(\\.[0-9]{1,17})?)\\])?");;
        static RandomValueGenerator getGenerator(String expression) {
            if ("uuid".equals(expression)) {
                return new RandomUUIDGenerator();
            } else if (expression.startsWith("random.string")) {
                Matcher matcher = RANDOM_STRING_PATTERN.matcher(expression);
                if (matcher.find()) {
                    String minStr = matcher.group("min");
                    String maxStr = matcher.group("max");
                    int min, max;
                    max = min = Integer.valueOf(minStr);
                    if (null != maxStr) max = Integer.valueOf(maxStr);
                    return new RandomStringGenerator(min, max);
                } else {
                    throw new RuntimeException("parameters of random.string do NOT conform to the specifications");
                }
            } else if (expression.startsWith("random.integer")) {
                if ("random.integer".length() == expression.length()) return new RandomIntegerGenerator(0, Integer.MAX_VALUE);
                Matcher matcher = RANDOM_INTEGER_PATTERN.matcher(expression);
                if (matcher.find()) {
                    int min = Integer.valueOf(matcher.group("min"));
                    int max = Integer.valueOf(matcher.group("max"));
                    return new RandomIntegerGenerator(min, max);
                } else {
                    throw new RuntimeException("parameters of random.integer do NOT conform to the specifications");
                }
            } else if (expression.startsWith("random.long")) {
                if ("random.long".length() == expression.length()) return new RandomLongGenerator(0, Long.MAX_VALUE);
                Matcher matcher = RANDOM_LONG_PATTERN.matcher(expression);
                if (matcher.find()) {
                    long min = Long.valueOf(matcher.group("min"));
                    long max = Long.valueOf(matcher.group("max"));
                    return new RandomLongGenerator(min, max);
                } else {
                    throw new RuntimeException("parameters of random.long do NOT conform to the specifications");
                }
            } else if (expression.startsWith("random.double")) {
                if ("random.double".length() == expression.length()) return new RandomDoubleGenerator(0.0, Double.MAX_VALUE, 1);
                Matcher matcher = RANDOM_DOUBLE_PATTERN.matcher(expression);
                if (matcher.find()) {
                    String minString = matcher.group("min");
                    String maxString = matcher.group("max");
                    int decimalPartLen = Math.max(decimalPartLength(minString), decimalPartLength(maxString));
                    double min = Double.valueOf(minString);
                    double max = Double.valueOf(maxString);
                    return new RandomDoubleGenerator(min, max, decimalPartLen);
                } else {
                    throw new RuntimeException("parameters of random.double do NOT conform to the specifications");
                }
            }/* else if (expression.startsWith("random.float")) {

            }*/
            throw new RuntimeException("can NOT press expression: " + expression);
        }

        private static int decimalPartLength(String doubleString) {
            if (null == doubleString) throw new IllegalArgumentException("null string has no decimal part");
            String decimal = doubleString.replaceFirst("^.*\\.", "");
            return decimal.length();
        }
    }
}
