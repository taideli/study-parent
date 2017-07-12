package com.tdl.study.test.crawler;

import com.tdl.study.crawler.WebDriverPooledFactory;
import com.tdl.study.crawler.WebDriverUtil;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverPoolTest {

    @Test
    public void driverPoolTest() throws Exception {
        GenericObjectPool<WebDriver> webDriverPool = new GenericObjectPool<>(new WebDriverPooledFactory());
        webDriverPool.setMaxTotal(20); // 最多能放多少个对象
        webDriverPool.setMinIdle(5);   // 最少有几个闲置对象
        webDriverPool.setMaxIdle(20); // 最多允许多少个闲置对象
        webDriverPool.preparePool();
        WebDriver driver = webDriverPool.borrowObject();
        System.out.println("before get, title:" + driver.getTitle());
        driver.get("http://www.baidu.com");
        new WebDriverWait(driver, 10).until(d -> !d.getTitle().equals(""));
//        new WebDriverWait(driver, 10);

        System.out.println(driver.getPageSource());
        webDriverPool.returnObject(driver);
        webDriverPool.close();
    }

    @Test
    public void property() {
        System.out.println(System.getProperty(WebDriverUtil.PHANTOMJS_PATH_KEY));
    }
}
