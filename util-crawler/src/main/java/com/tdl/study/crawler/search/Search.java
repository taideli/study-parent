package com.tdl.study.crawler.search;

import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.Callable;

public class Search implements Callable<Long> {
    private JSONObject conf;
    private final String seed, keyword;

    public Search(final JSONObject conf, String seed, String keyword) {
        this.conf = conf;
        this.seed = seed;
        this.keyword = keyword;
    }

    @Override
    public Long call() {
        System.out.println("search [" + keyword + "] on " + seed);
        // TODO replace this with Concurrents.sleep();
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " finished...");

        return 22L ;
    }
}
