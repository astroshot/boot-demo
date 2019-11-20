package com.boot.common.rpc.http.annotation;

import com.boot.common.rpc.http.constant.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTPRequest {

    RequestMethod method() default RequestMethod.GET;

    String url() default "";

    String urlKey() default "";

}
