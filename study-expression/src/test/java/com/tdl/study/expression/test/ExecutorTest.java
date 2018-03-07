package com.tdl.study.expression.test;

import com.tdl.study.expression.Executor;
import com.tdl.study.expression.exception.InvalidExpressionException;
import org.junit.Test;

import java.io.IOException;

public class ExecutorTest {

    @Test
    public void t1() throws IOException {
        try {
            Object v = Executor.execute("2 + 5");
            System.out.println("value: " + v);
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
        }
    }
}
