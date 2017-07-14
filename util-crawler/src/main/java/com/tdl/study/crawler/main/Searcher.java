package com.tdl.study.crawler.main;

import com.tdl.study.crawler.search.Search;
import com.tdl.study.crawler.util.JSONUtil;
import static com.tdl.study.crawler.Constants.*;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Searcher {

    private final JSONObject conf;
    private final String rootUrl;


    public Searcher(JSONObject conf) {
        this.conf = conf;
        this.rootUrl = conf.getString(CONF_KEY_ROOT_URL);
    }

    public void searchAll() {
        List<Callable<Long>> searchs =  conf.getJSONArray(CONF_KEY_SEARCH_WORDS).toList()
                .stream()
                .map(Object::toString)
                .map(keyword -> new Search(conf, rootUrl, keyword))
                .collect(Collectors.toList());

        ExecutorService es = Executors.newFixedThreadPool(searchs.size());

        try {
            List<Future<Long>> futures = es.invokeAll(searchs);
            if (!futures.stream().map(Future::isDone).reduce((r1, r2) -> r1 && r2).orElse(false)) {
                System.out.println("some job execute failed");
            } else {
                System.out.println("finished... total: " + futures.stream().map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return 0L;
                    }
                }).reduce((r1, r2) -> r1 + r2).orElse(0L));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
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
