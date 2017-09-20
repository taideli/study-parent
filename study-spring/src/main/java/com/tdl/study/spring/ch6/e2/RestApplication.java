package com.tdl.study.spring.ch6.e2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication /*application.properties只有放在resources根目录才能被自动找到*/
@PropertySource("classpath:com/tdl/study/spring/ch6/e2/application.properties")
// ↑ 指定配置文件的位置
public class RestApplication {

    private static final String BANNER = "     _             _                        _             \n" +
            "    | |           | |                      (_)            \n" +
            " ___| |_ _   _  __| |_   _   ___ _ __  _ __ _ _ __   __ _ \n" +
            "/ __| __| | | |/ _` | | | | / __| '_ \\| '__| | '_ \\ / _` |\n" +
            "\\__ \\ |_| |_| | (_| | |_| | \\__ \\ |_) | |  | | | | | (_| |\n" +
            "|___/\\__|\\__,_|\\__,_|\\__, | |___/ .__/|_|  |_|_| |_|\\__, |\n" +
            "                      __/ |     | |                  __/ |\n" +
            "                     |___/      |_|                 |___/ ";

    @Value("${book.author}")
    private String bookAuthor;

    @Value("${book.name}")
    private String bookName;

    @RequestMapping("/")
    String index() {
//        return "Hello Spring Boot";
        return "book name: " + bookName + ", author: " + bookAuthor;
    }

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(RestApplication.class);
        application.setBanner((environment, sourceClass, out) -> out.println(BANNER));
        application.run();
    }
}
