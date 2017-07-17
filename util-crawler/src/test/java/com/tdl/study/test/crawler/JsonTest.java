package com.tdl.study.test.crawler;

import com.tdl.study.crawler.util.JSONUtil;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.junit.Test;

public class JsonTest {

    @Test
    public void load() {
        JSONObject obj = JSONUtil.load("C:/Users/taidl/Desktop/baidu.json");
        System.out.println(obj.toString(2));
    }

    @Test
    public void jsonPointer() {
        JSONObject obj = JSONUtil.load("C:/Users/taidl/Desktop/baidu-simple.json");
        JSONPointer pointer1 = new JSONPointer("/searchTarget");
        JSONPointer pointer2 = new JSONPointer("/properties/1");
        Object o1 = pointer1.queryFrom(obj);
        Object o2 = pointer2.queryFrom(obj);
        System.out.println(o1.toString());
        System.out.println(o2.toString());
    }

    @Test
    public void jsonWriter() {
        JSONWriter writer = new JSONStringer().object();
        writer.key("long").value(9L);
        JSONObject o = new JSONObject();
        o.put("k1", "v1");
        o.put("k2", "v2");
        writer.key("actions").array().value(o).endArray();
        System.out.println(writer.endObject());
    }
}
