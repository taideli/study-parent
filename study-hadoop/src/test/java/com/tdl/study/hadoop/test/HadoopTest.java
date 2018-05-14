package com.tdl.study.hadoop.test;

import org.junit.Test;

public class HadoopTest {

    @Test
    public void t1() {
        String s = "good      morning   everyone";
        String[] sArrary = s.split("\\s+");
        for (int i = 0; i < sArrary.length; i++) {
            System.out.println("`" + sArrary[i] + "`");
        }
        String[] aa = new String[]{"a", "b", "c"};
        System.out.println(aa.getClass());
    }
}
