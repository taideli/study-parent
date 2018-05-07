package com.tdl.study.java.test.cm;

import com.tdl.study.java.compile.CompileManager;

import java.io.IOException;

public class TestCompileManger {
    public static void main(String[] args) throws IOException {
        CompileManager cm = new CompileManager();
        cm.addSourceFile("study-java/src/test/java/com/tdl/study/java/test/cm/CMTest.java");
        cm.compile();
    }
}
