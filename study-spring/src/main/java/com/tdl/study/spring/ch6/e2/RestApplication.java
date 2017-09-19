package com.tdl.study.spring.ch6.e2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RestApplication {

    private static final String BANNER = "     _             _                        _             \n" +
            "    | |           | |                      (_)            \n" +
            " ___| |_ _   _  __| |_   _   ___ _ __  _ __ _ _ __   __ _ \n" +
            "/ __| __| | | |/ _` | | | | / __| '_ \\| '__| | '_ \\ / _` |\n" +
            "\\__ \\ |_| |_| | (_| | |_| | \\__ \\ |_) | |  | | | | | (_| |\n" +
            "|___/\\__|\\__,_|\\__,_|\\__, | |___/ .__/|_|  |_|_| |_|\\__, |\n" +
            "                      __/ |     | |                  __/ |\n" +
            "                     |___/      |_|                 |___/ ";

    @RequestMapping("/")
    String index() {
        return "Hello Spring Boot";
    }

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(RestApplication.class, args);
        application.setBanner((environment, sourceClass, out) -> out.println(BANNER));
        application.run();
    }
}
