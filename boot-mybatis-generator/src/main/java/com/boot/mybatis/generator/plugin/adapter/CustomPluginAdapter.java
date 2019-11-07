package com.boot.mybatis.generator.plugin.adapter;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

public abstract class CustomPluginAdapter extends PluginAdapter {

    protected String getTableName(IntrospectedTable introspectedTable) {
        return introspectedTable.getTableConfiguration().getTableName().replaceAll("([0-9]+)$", "");
    }
}
