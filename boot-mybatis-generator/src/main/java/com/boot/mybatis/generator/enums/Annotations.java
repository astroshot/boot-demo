package com.boot.mybatis.generator.enums;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Collection;
import java.util.Collections;

public enum Annotations {

    GETTER("getter", "@Getter", "lombok.Getter"),
    SETTER("setter", "@Setter", "lombok.Setter"),
    DATA("data", "@Data", "lombok.Data"),
    BUILDER("builder", "@Builder", "lombok.Builder"),
    ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
    NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
    TO_STRING("toString", "@ToString", "lombok.ToString");

    private final String paramName;
    private final String name;
    private final FullyQualifiedJavaType javaType;

    Annotations(String paramName, String name, String className) {
        this.paramName = paramName;
        this.name = name;
        this.javaType = new FullyQualifiedJavaType(className);
    }

    public String getParamName() {
        return paramName;
    }

    public String getName() {
        return name;
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }

    public static Annotations getValueOf(String paramName) {
        for (Annotations annotation : Annotations.values()) {
            if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                return annotation;
            }
        }
        return null;
    }

    public static Collection<Annotations> getDependencies(Annotations annotation) {
        if (annotation == ALL_ARGS_CONSTRUCTOR) {
            return Collections.singleton(NO_ARGS_CONSTRUCTOR);
        } else {
            return Collections.emptyList();
        }
    }
}
