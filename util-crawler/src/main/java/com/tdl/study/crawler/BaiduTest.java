package com.tdl.study.crawler;

import com.tdl.study.crawler.output.FileStore;
import com.tdl.study.crawler.output.Store;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaiduTest {
    public static void main(String[] args) {
        WebDriver driver = WebDriverPool.borrowOne();
        String url = "http://www.baidu.com";
        String keyWords = "丝绸制品 差";
        driver.get(url);

//        driver.switchTo().window();
        String title = driver.getTitle();
        System.out.println(title);
//        WebElement kw = driver.findElementById("kw");
        WebElement kw = driver.findElement(By.cssSelector("input[id=kw]"));
        kw.sendKeys(keyWords);
        kw.submit();

        new WebDriverWait(driver, 10).until(d -> !d.getTitle().equals(title));
        System.out.println(driver.getTitle());
        List<WebElement> elements = driver.findElements(By.cssSelector("div[tpl=se_com_default]"));
        List<JSONObject> js = new ArrayList<>();

        for (WebElement element : elements) {
            WebElement a = element.findElement(By.cssSelector("a"));
            String t = a.getText();
            String href = a.getAttribute("href");

            JSONObject obj = new JSONObject();
            obj.put("title", t);
            obj.put("link", href);
            obj.put("overview", element.findElement(By.cssSelector("div[class=c-abstract]")).getText());

            js.add(obj);
//            System.out.println("title   : " + t);
//            System.out.println("href    : " + href);
//            System.out.println("overview: " + element.findElement(By.cssSelector("div[class=c-abstract]")).getText());
//            System.out.println();
        }

        Store<String> store = new FileStore("E:\\writeTest\\" + /*url*/ "百度"+ "-" + keyWords + ".json");
        store.prepare();

        store.write(js.stream().map(JSONObject::toString).collect(Collectors.toList()));
        store.close();
        driver.getWindowHandles().forEach(System.out::println);

        System.out.println("size...." + driver.getWindowHandles().size());
        /*System.out.println("page2..............................................");
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
        }*/

        driver.getWindowHandles().forEach(System.out::println);
        driver.close();
    }
}
