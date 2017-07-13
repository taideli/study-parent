package com.tdl.study.crawler.main;

import com.tdl.study.crawler.JsoupTest;
import com.tdl.study.crawler.WebDriverPool;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public class PhantomJsTest {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = WebDriverPool.borrowOne();

        driver.navigate().to("https://www.taobao.com/");
        String title = driver.getTitle();
        System.out.println("title: " + title);
        String ps = driver.getPageSource();
        System.out.println("page source length: " + ps.length());
        try {
            JsoupTest.getAllLinks(Jsoup.parse(ps));
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebElement element = driver.findElement(By.id("kw"));
        CharSequence searchKey="东北大学";
        element.sendKeys(searchKey);
        element.submit();

//        new WebDriverWait(driver, 10).until(d -> !d.getTitle().equals(title));
//        System.out.println("new page title is: " + driver.getTitle());
        WebDriverPool.returnOne(driver);
    }
}