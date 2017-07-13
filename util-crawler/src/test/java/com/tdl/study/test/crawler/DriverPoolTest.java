package com.tdl.study.test.crawler;

import com.tdl.study.crawler.WebDriverFactory;
import com.tdl.study.crawler.WebDriverPool;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverPoolTest {

    @Test
    public void driverPoolTest() throws Exception {

        WebDriver driver = WebDriverPool.borrowOne();
        System.out.println("before get, title:" + driver.getTitle());
        driver.get("http://www.baidu.com");
        new WebDriverWait(driver, 10).until(d -> !d.getTitle().equals(""));
//        new WebDriverWait(driver, 10);

        System.out.println(driver.getPageSource());
        WebDriverPool.returnOne(driver);
    }

    @Test
    public void property() {
        System.out.println(System.getProperty(WebDriverFactory.PHANTOMJS_PATH_KEY));
    }

    @Test
    public void borrowAndReturn() {
        WebDriver driver = WebDriverPool.borrowOne();
        WebDriverPool.returnOne(driver);
    }
}
