package com.boot.mybatis.generator.enums;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

public enum SwaggerAnnotation {

    API_MODEL("ApiModel", "@ApiModel", "io.swagger.annotations.ApiModel"),
    API_MODEL_PROPERTY("ApiModelProperty", "@ApiModelProperty", "io.swagger.annotations.ApiModelProperty"),
    ;

    private final String paramName;
    private final String name;
    private final FullyQualifiedJavaType javaType;

    SwaggerAnnotation(String paramName, String name, String className) {
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

    public static SwaggerAnnotation getValueOf(String paramName) {
        for (SwaggerAnnotation annotation : values()) {
            if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                return annotation;
            }
        }
        return null;
    }

}
