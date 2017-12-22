package com.tdl.study.core.test.conf;

import com.tdl.study.core.conf.Configs;
import com.tdl.study.core.utils.Systems;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tdl.study.core.conf.Configs.Config;

@Config(value = "test.properties", prefix = "conf.test", ignoreSystemFields = true)
public class Test {
    public static void main(String[] args) {

        Configs.map().forEach((k, v) -> System.out.println(k + " <====> " + v));

        System.out.println(Systems.getMainClass());

//        String line = "tdl.config=application.properties  #         default file name abc";
//        String line = "tdl.config=app.config";
//        String line = "tdl.config=app.config# #sss";
        String line = "tdl.config=zk://#fragment #aa";
//        String line = "a=";
//        Pattern key = Pattern.compile("([a-zA-z0-9]+([.][a-zA-Z0-9]+)*)=(.+)\\s+([#]\\s*(.*))+");
        Pattern key = Pattern.compile("(^[a-zA-z0-9]+([.][a-zA-Z0-9]+)*)=([^\\s]+)(\\s+[#]\\s*(.*))?");
        Matcher matcher = key.matcher(line);
        while (matcher.find()) {
            String k = matcher.group(1);
            String v = matcher.group(3);
            String a = matcher.group(5);
            System.out.println("key:" + k);
            System.out.println("value:" + v);
            System.out.println("annotation:" + a);
        }
    }
}
