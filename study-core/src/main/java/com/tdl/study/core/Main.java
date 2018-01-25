package com.tdl.study.core;

import com.tdl.study.core.conf.RandomValueGenerator;
import com.tdl.study.core.conf.TConfigs;
import com.tdl.study.core.utils.Threads;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@TConfigs.Config(value = "test.properties", ignoreSystemFields = true)
public class Main {

    public static void main(String[] args) {

/*
        System.out.println("xxx = " + TConfigs.get("xxx", (String) null));
        System.out.println("xxx = " + TConfigs.get("xxx", "hahaha"));
        System.out.println("xxx = " + TConfigs.get("conf.test.alpha", "hahaha"));
        System.out.println("xxx = " + TConfigs.get("conf.test.beta", "hahaha"));
        Integer num = TConfigs.get("conf.test.alpha", Integer.class, 888);
        System.out.println("num: " + num);
*/
//        Threads.sleep(3 * 1000);
//        System.out.println("--------------map:\n" + TConfigs.map());
//        String str = "uuid";
//        String str = "random.integer[1,50]";
//        String str = "random.long[1,50564168464]";
        String str = "random.double[1,50453558.0]"; // TODO: 2018/1/8 bugs
//        String str = "random.string[1,66]";

        int idx = 0;
        while (idx++ < 100) {
            RandomValueGenerator generator = RandomValueGenerator.of(str);
            System.out.println(generator.generate());
        }
    }
}
