package com.tdl.study.spring.ch6.e4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication /*application.properties只有放在resources根目录才能被自动找到*/
@PropertySource("classpath:com/tdl/study/spring/ch6/e4/application.properties")
public class ProfilesApp {

    @RequestMapping("/")
    String index() {
        return "Hello Spring Boot ch6 e4";
    }

    public static void main(String[] args) {
        SpringApplication.run(ProfilesApp.class, args);
    }
}

/** 这三个文件放在根目录下可以*/

/** 上面的配置不生效的，配置文件查找路径如下：
* profile的配置文件可以按照application.properies的放置位置一样，放于以下四个位置，
*
*   当前目录的 “/config”的子目录下
*   当前目录下
*   classpath根目录的“/config”包下
*   classpath的根目录下
*/
