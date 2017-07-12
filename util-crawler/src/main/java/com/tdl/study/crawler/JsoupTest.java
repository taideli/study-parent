package com.tdl.study.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupTest {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.taobao.com/").get();
        getAllLinks(doc);
    }
    public static void getAllLinks(Document doc) throws IOException {

        Elements links = doc.select("a[href]");
        System.out.println("\nLinks: " + links.size());
        links.forEach(link -> {
            System.out.println(String.format("a:<%s> (%s)", link.attr("abs:href"), link.text()));
        });
    }
}
