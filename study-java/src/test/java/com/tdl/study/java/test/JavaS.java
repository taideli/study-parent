package com.tdl.study.java.test;

import org.junit.Test;

public class JavaS {

    @Test
    public void finallyTest() {
        try {
            System.out.println("trying");
            return;
        } finally {
            System.out.println("finally...");
        }
    }
}
