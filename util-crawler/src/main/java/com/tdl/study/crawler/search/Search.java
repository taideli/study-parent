package com.tdl.study.crawler.search;

import com.tdl.study.crawler.WebDriverPool;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.Callable;

import static com.tdl.study.crawler.Constants.CONF_KEY_ELEMENTS_TARGET;
import static com.tdl.study.crawler.Constants.CONF_KEY_RESULT_PREFIX;
import static com.tdl.study.crawler.Constants.CONF_KEY_SEARCH_TARGET;

public class Search implements Callable<Long> {
    private JSONObject conf;
    private final String seed, keyword;
    private final String filePrefix;

    private By targetBy;

    public Search(final JSONObject conf, String seed, String keyword) {
        this.conf = conf;
        this.seed = seed;
        this.keyword = keyword;
        this.filePrefix = conf.getString(CONF_KEY_RESULT_PREFIX);
        targetBy = parseBy(conf.getString(CONF_KEY_SEARCH_TARGET));
    }

    @Override
    public Long call() {
        System.out.println("search [" + keyword + "] on " + seed);

        WebDriver driver = WebDriverPool.borrowOne();
        driver.get(seed + "/s?wd=" + keyword);

//        long resultCount = processPage(driver.findElement(By.xpath("/")));
        long resultCount = processPage(driver.findElement(By.tagName("body")));
//        List<WebElement> els = driver.findElements(targetBy);
        // process each element in new thread. or into a list



        WebDriverPool.returnOne(driver);
        System.out.println("---process of " + keyword + "      size :" + resultCount);
        return resultCount;
    }

    private long processPage(WebElement e) {
//        System.out.println("element-------------------------: \n" + e.getText());
        List<WebElement> els = e.findElements(parseBy(conf.getString(CONF_KEY_ELEMENTS_TARGET)));

        els.forEach(element -> System.out.println("\n\n" + element.getText()));
//        WebElement nextPage = e.findElement(By.xpath(""));
        return els.size();
    }

    public static By parseBy(String desc) {
        String[] sa = desc.split("=", 2);
        if (sa.length != 2) throw new RuntimeException("wrong config of parse: " + desc);
        switch (sa[0]) {
            case "xpath" :
                return By.xpath(sa[1]);
            case "css" :
                return By.cssSelector(sa[1]);
            case "attr" :
            default :
                throw new RuntimeException("selector [" + sa[0] + "] not support for now");
        }
    }
}
