package com.tdl.study.crawler.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONUtil {

    public static JSONObject load(String pathname) {
        String str = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
            str = reader.lines().reduce((s1, s2) -> s1 + s2).orElse("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(str);
    }
}
