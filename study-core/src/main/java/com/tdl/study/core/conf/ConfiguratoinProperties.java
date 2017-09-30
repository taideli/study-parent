package com.tdl.study.core.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfiguratoinProperties {
    String value() default "";

    String prifix() default "";

    boolean ignoreInvalidFields() default false;


    // TODO: 2017/9/30 参照spring boot同名类实现
}
