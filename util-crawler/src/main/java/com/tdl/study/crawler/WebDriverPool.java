package com.tdl.study.crawler;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.WebDriver;

public final class WebDriverPool {
    private static final GenericObjectPool<WebDriver> webDriverPool;

    static {
        webDriverPool = new GenericObjectPool<>(new WebDriverPooledFactory());
        webDriverPool.setMaxTotal(Integer.parseInt(System.getProperty(
            "webdriver.pool.max.total", "20"))); // 最多能放多少个对象
        webDriverPool.setMinIdle(Integer.parseInt(System.getProperty(
            "webdriver.pool.min.idle", "5")));   // 最少有几个闲置对象
        webDriverPool.setMaxIdle(Integer.parseInt(System.getProperty(
                "webdriver.pool.max.idle", "20"))); // 最多允许多少个闲置对象
        try {
            webDriverPool.preparePool();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver borrowOne() {
        try {
            return webDriverPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void returnOne(WebDriver driver) {
        webDriverPool.returnObject(driver);
    }
}
