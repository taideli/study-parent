package com.tdl.study.tools.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestDemo {

    @Test
    public void t1() {
        Map<String, Integer> header = new HashMap<>();
        header.put("abc", 1);
        header.put("bcd", 2);
        header.put("cde", 3);

        System.out.println(Arrays.toString(header.keySet().toArray()));
    }
}
