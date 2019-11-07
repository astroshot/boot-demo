package com.boot.mybatis.generator.api;

import org.mybatis.generator.api.dom.java.Method;

public class InterfaceExtendedProperty extends Method {

    private String formattedContent;

    @Override
    public String getFormattedContent(int indentLevel, boolean interfaceMethod) {
        return formattedContent;
    }

    public String getFormattedContent() {
        return formattedContent;
    }

    public void setFormattedContent(String formattedContent) {
        this.formattedContent = formattedContent;
    }

}
