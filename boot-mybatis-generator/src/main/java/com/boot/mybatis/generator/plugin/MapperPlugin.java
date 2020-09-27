package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.api.InterfaceExtendedProperty;
import com.boot.mybatis.generator.plugin.adapter.CustomPluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;

public class MapperPlugin extends CustomPluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    protected String join(List<String> array) {
        return join(array, ", ");
    }

    protected String join(List<String> array, String separator) {
        if (array == null || array.size() == 0) {
            return "";
        }

        if (separator == null) {
            separator = ",";
        }

        StringBuilder sb = new StringBuilder();

        int total = array.size();
        for (int i = 0; i < total; i++) {
            if (i != 0) {
                sb.append(separator);
            }
            sb.append(array.get(i));
        }
        return sb.toString();
    }

    @Override
    public boolean clientGenerated(
            Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();

        List<String> columnNames = new ArrayList<>();
        for (IntrospectedColumn item : columns) {
            columnNames.add(item.getActualColumnName());
        }

        String propertyFmt = "    public static final String %s = \"%s\";";
        InterfaceExtendedProperty extendedColumnProperty = new InterfaceExtendedProperty();
        extendedColumnProperty.setFormattedContent(String.format(propertyFmt, "COLUMNS", join(columnNames)));
        interfaze.addMethod(extendedColumnProperty);

        InterfaceExtendedProperty extendedTableNameProperty = new InterfaceExtendedProperty();
        extendedTableNameProperty.setFormattedContent(
                String.format(propertyFmt, "TABLE_NAME", getTableName(introspectedTable)));
        interfaze.addMethod(extendedTableNameProperty);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

}
