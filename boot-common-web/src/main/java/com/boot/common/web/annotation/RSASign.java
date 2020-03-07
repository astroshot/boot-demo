package com.boot.common.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RSA signature check annotation, bind to Controller class or method.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RSASign {

    boolean required() default true;

    /**
     * key of signature
     */
    String name() default "signature";
}
