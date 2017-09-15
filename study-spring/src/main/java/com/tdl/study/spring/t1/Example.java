package com.tdl.study.spring.t1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Example {

    /**
     * \@RequestMapping 注解提供路由信息。 它告诉Spring任何来自"/"路径的HTTP请求都应该被映射到 home 方法。
     * \@RestController 注解告诉Spring以字符串的形式渲染结果， 并直接返回给调用者
     * @return
     */
    @RequestMapping("/")
    String home() {
        return "Hello spring";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}
