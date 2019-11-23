package com.boot.common.rpc.http.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {

    /**
     * Parameter name
     */
    String value() default "";

    /**
     * Parameter name
     */
    @AliasFor("name")
    String name() default "";

}
