package com.boot.common.rpc.http.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {

    /**
     * Request parameter name
     */
    String value() default "";

    /**
     * Request parameter name
     */
    @AliasFor("name")
    String name() default "";

    /**
     * Set `true` if this param is placed in HTTP header
     */
    boolean inHeader() default false;
}
