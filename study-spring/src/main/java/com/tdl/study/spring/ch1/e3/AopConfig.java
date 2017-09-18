package com.tdl.study.spring.ch1.e3;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/* 5. 配置类*/
@Configuration
@ComponentScan("com.tdl.study.spring.ch1.e3")
@EnableAspectJAutoProxy
public class AopConfig {
}
