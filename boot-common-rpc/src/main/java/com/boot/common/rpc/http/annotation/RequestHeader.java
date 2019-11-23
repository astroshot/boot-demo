package com.boot.common.rpc.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestHeader {

    /**
     * key in header
     */
    String key() default "";

    /**
     * value of key in header
     */
    String value() default "";
}
