package com.tdl.study.core.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configure {

    /**
     * filename
     * @return
     */
    String value() default "";

    /**
     * prefix
     * @return
     */
    String prefix() default "";
}
