package com.tdl.study.core.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigureKey {

    /**
     * if is default value, get field name as key
     * @return
     */
    String value() default "";

    Class<?> type() default Object.class;
}
