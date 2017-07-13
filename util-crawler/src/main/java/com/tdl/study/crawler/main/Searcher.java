package com.tdl.study.crawler.main;

import com.tdl.study.crawler.util.JSONUtil;
import static com.tdl.study.crawler.Constants.*;

import org.json.JSONObject;

public class Searcher {

    private final JSONObject conf;
    private final String rootUrl;


    public Searcher(JSONObject conf) {
        this.conf = conf;
        this.rootUrl = conf.getString(CONF_KEY_ROOT_URL);

    }

    public void searchAll() {
        conf.getJSONArray(CONF_KEY_SEARCH_WORDS).forEach(o -> search(o.toString()));
    }


    public void search(String key) {
        System.out.println("===========start search key word: \"" + key + "\"");
    }

    public static void main(String[] args) {
        /*if (null == args || args.length < 1) {
            help();
            System.exit(1);
        }
        String confJsonPath = args[0];*/
        String confJsonPath = "C:/Users/taidl/Desktop/baidu-simple.json";
        JSONObject conf = JSONUtil.load(confJsonPath);
        System.out.println(conf.toString(2));
        new Searcher(conf).searchAll();
    }

    private static void help() {
        System.err.println("Usage: \n    "
            + Searcher.class.getName() + " <conf.json>");
    }
}
