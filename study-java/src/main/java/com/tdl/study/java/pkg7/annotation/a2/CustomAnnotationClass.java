package com.tdl.study.java.pkg7.annotation.a2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 1. 关键字：@interface说明这是一个自定义注解的定义。
 * 2. 为此注解定义一对强制性的属性，保留策略(@Retention)和目标(@Target)。还有一些其他属性可以定义，不过这两个是最基本和重要的
 * 3. 自定义两个注解的成员(author(), date()) 所有的方法声明都不能有参数和throw子句
 * 4. 使用注解  在其它类中 @CustomAnnotationClass( date = "2014-05-05" )
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomAnnotationClass {
    public String author() default "danibuiza";
    public String date();
}
