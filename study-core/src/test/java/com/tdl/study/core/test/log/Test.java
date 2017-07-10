package com.tdl.study.core.test.log;

import com.tdl.study.core.log.Logger;

public class Test {
    private Logger logger = Logger.getLogger(getClass());

    public void exec() {
        logger.info("hello logger..");
    }

    public static void main(String[] args) {
        new Test().exec();
    }
}
