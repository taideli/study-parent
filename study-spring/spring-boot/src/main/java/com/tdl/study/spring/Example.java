package com.tdl.study.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Example {

    @Value("${example.r1}")
    private String r1;

    @Value("${example.r2}")
    private String r2;

    @Value("${example.r3}")
    private String r3;

    @RequestMapping("/")
    String home() {
        return "Hello World!" + String.format("r1=%s   r2=%s   r3=%s", r1, r2, r3);
    }

    public Example() {
        System.out.println("~~~~" + r1);
    }

    public static void main(String[] args) {
//        SpringApplication.run(Example.class, args);
        SpringApplication app = new SpringApplication(Example.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
