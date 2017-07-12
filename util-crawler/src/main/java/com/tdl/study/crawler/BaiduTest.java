package com.tdl.study.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BaiduTest {
    public static void main(String[] args) {
        WebDriver driver = WebDriverUtil.getWebDriver();

        driver.get("http://www.baidu.com");

//        driver.switchTo().window();
        String title = driver.getTitle();
        System.out.println(title);
//        WebElement kw = driver.findElementById("kw");
        WebElement kw = driver.findElement(By.cssSelector("input[id=kw]"));
        kw.sendKeys("丝绸制品 差");
        kw.submit();

        new WebDriverWait(driver, 10).until(d -> !d.getTitle().equals(title));
        System.out.println(driver.getTitle());
        List<WebElement> elements = driver.findElements(By.cssSelector("div[tpl=se_com_default]"));
        for (WebElement element : elements) {
            WebElement a = element.findElement(By.cssSelector("a"));
            String t = a.getText();
            String href = a.getAttribute("href");
//            String t = element.findElement(By.cssSelector("a")).getText();
            System.out.println("title   : " + t);
            System.out.println("href    : " + href);
            System.out.println("overview: " + element.findElement(By.cssSelector("div[class=c-abstract]")).getText());
            System.out.println();
        }

//        driver.findElement(By.xpath(""));
        System.out.println("page2..............................................");
        String url = driver.findElement(By.cssSelector("a[class=n]")).getAttribute("href");
        driver.get(url);
        List<WebElement> elements1 = driver.findElements(By.cssSelector("div[tpl=se_com_default]"));
        for (WebElement element : elements1) {
            WebElement a = element.findElement(By.cssSelector("a"));
            String t = a.getText();
            String href = a.getAttribute("href");
//            String t = element.findElement(By.cssSelector("a")).getText();
            System.out.println("title   : " + t);
            System.out.println("href    : " + href);
            System.out.println("overview: " + element.findElement(By.cssSelector("div[class=c-abstract]")).getText());
            System.out.println();
        }

        driver.getWindowHandles().forEach(System.out::println);
        driver.close();
    }
}
