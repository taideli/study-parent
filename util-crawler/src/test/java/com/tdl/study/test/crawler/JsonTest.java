package com.tdl.study.test.crawler;

import com.tdl.study.crawler.util.JSONUtil;
import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {

    @Test
    public void load() {
        JSONObject obj = JSONUtil.load("C:/Users/taidl/Desktop/baidu.json");
        System.out.println(obj.toString());
    }
}
