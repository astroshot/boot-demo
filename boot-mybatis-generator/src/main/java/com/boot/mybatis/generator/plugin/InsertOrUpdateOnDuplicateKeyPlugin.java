package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.plugin.adapter.CustomPluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class InsertOrUpdateOnDuplicateKeyPlugin extends CustomPluginAdapter {

    private static final String METHOD_NAME = "insertOrUpdateOnDuplicateKey";

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addInsertOnDuplicateKeyMethod(interfaze, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        addInsertOnDuplicateKeyXml(document, introspectedTable);
        return true;
    }

    protected void addInsertOnDuplicateKeyMethod(Interface interfaze, IntrospectedTable introspectedTable) {
        // import necessary classes
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(introspectedTable.getRules().calculateAllFieldsClass());
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getIntInstance();

        // add method `batchInsert`
        Method insertOrUpdateOnDuplicateKeyMethod = new Method();
        // set method to public
        insertOrUpdateOnDuplicateKeyMethod.setVisibility(JavaVisibility.PUBLIC);
        // set return value type
        insertOrUpdateOnDuplicateKeyMethod.setReturnType(returnType);
        // set method name
        insertOrUpdateOnDuplicateKeyMethod.setName(METHOD_NAME);
        // set parameters
        FullyQualifiedJavaType paramType = introspectedTable.getRules().calculateAllFieldsClass();
        insertOrUpdateOnDuplicateKeyMethod.addParameter(new Parameter(paramType, "record", "@Param(\"record\")"));
        insertOrUpdateOnDuplicateKeyMethod.addParameter(
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "columns", "@Param(\"columns\")", true));
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(insertOrUpdateOnDuplicateKeyMethod);
    }

    protected void addInsertOnDuplicateKeyXml(Document document, IntrospectedTable introspectedTable) {
        XmlElement insertOnDuplicateKey = new XmlElement("insert");
        insertOnDuplicateKey.addAttribute(new Attribute("id", METHOD_NAME));
        insertOnDuplicateKey.addAttribute(new Attribute("parameterType", "map"));

        insertOnDuplicateKey.addElement(
                new TextElement(
                        "insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement valueTrimElement = new XmlElement("trim");
        valueTrimElement.addAttribute(new Attribute("prefix", " ("));
        valueTrimElement.addAttribute(new Attribute("suffix", ")"));
        valueTrimElement.addAttribute(new Attribute("suffixOverrides", ","));

        XmlElement columnTrimElement = new XmlElement("trim");
        columnTrimElement.addAttribute(new Attribute("prefix", "("));
        columnTrimElement.addAttribute(new Attribute("suffix", ")"));
        columnTrimElement.addAttribute(new Attribute("suffixOverrides", ","));

        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : allColumns) {
            String columnName = introspectedColumn.getActualColumnName();
            if ("id".equals(columnName)) {
                continue;
            }
            columnTrimElement.addElement(new TextElement("`" + columnName + "`" + ","));
            valueTrimElement.addElement(
                    new TextElement("#{record."
                            + introspectedColumn.getJavaProperty()
                            + ",jdbcType="
                            + introspectedColumn.getJdbcTypeName()
                            + "},"));
        }

        insertOnDuplicateKey.addElement(columnTrimElement);
        insertOnDuplicateKey.addElement(new TextElement(" values "));
        insertOnDuplicateKey.addElement(valueTrimElement);
        insertOnDuplicateKey.addElement(new TextElement(" on duplicate key update "));

        XmlElement updateColumnTrimElement = new XmlElement("trim");
        updateColumnTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        XmlElement foreachColumnForValue = new XmlElement("foreach");
        foreachColumnForValue.addAttribute(new Attribute("collection", "columns"));
        foreachColumnForValue.addAttribute(new Attribute("index", "index"));
        foreachColumnForValue.addAttribute(new Attribute("item", "column"));
        List<IntrospectedColumn> onUpdateColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : onUpdateColumns) {
            String columnName = introspectedColumn.getActualColumnName();
            XmlElement check = new XmlElement("if");
            check.addAttribute(new Attribute("test", "'" + columnName + "' == column"));
            check.addElement(
                    new TextElement("`" + columnName + "`"
                            + " = #{record." + introspectedColumn.getJavaProperty() + "}, ")
            );
            foreachColumnForValue.addElement(check);
        }
        updateColumnTrimElement.addElement(foreachColumnForValue);
        insertOnDuplicateKey.addElement(updateColumnTrimElement);
        document.getRootElement().addElement(insertOnDuplicateKey);
    }
}
