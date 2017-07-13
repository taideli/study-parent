package com.tdl.study.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TaobaoTest {
    public static void main(String[] args) {
        WebDriver driver = WebDriverPool.borrowOne();

        driver.get("https://www.taobao.com");

        String title = driver.getTitle();
        System.out.println(title);
        WebElement phone = driver.findElement(By.linkText("手机"));
        phone.click();

        new WebDriverWait(driver, 10).until(d -> !d.getTitle().equals(title));
        System.out.println(driver.getTitle());

        List<WebElement> elements = driver.findElements(By.cssSelector("div[class=item]"));
        for (WebElement element : elements) {
            String link = element.findElement(By.cssSelector("a")).getAttribute("href");
            String tit = element.findElement(By.cssSelector("p[class=tit]")).getText();
            String des = element.findElement(By.cssSelector("p[class=des]")).getText();
            WebElement priceElement = element.findElement(By.cssSelector("p[class=price]"));
            String oldPrice = priceElement.findElement(By.cssSelector("span[class=old-price]")).getText();
            String price = priceElement.getText().replace(oldPrice, "");

            System.out.println("link     : " + link);
            System.out.println("title    : " + tit);
            System.out.println("desc     : " + des);
            System.out.println("price    : " + price);
            System.out.println("old-price: " + oldPrice);
            System.out.println();
        }

//
        driver.close();
    }
}
