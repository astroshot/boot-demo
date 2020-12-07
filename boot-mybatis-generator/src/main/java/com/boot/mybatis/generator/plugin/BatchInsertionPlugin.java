package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.plugin.adapter.EnhancedPluginAdapter;
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

public class BatchInsertionPlugin extends EnhancedPluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addBatchInsertionMethod(interfaze, introspectedTable);
        return true;
    }

    private void addBatchInsertionMethod(Interface interfaze, IntrospectedTable introspectedTable) {
        // 设置需要import的类
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getIntInstance();

        // add method `batchInsert`
        Method batchInsertMethod = new Method();
        // set method to public
        batchInsertMethod.setVisibility(JavaVisibility.PUBLIC);
        // set return value type
        batchInsertMethod.setReturnType(returnType);
        // set method name
        batchInsertMethod.setName("batchInsert");
        // set parameters
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType paramListType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        paramType.addTypeArgument(paramListType);
        batchInsertMethod.addParameter(new Parameter(paramType, "list", "@Param(\"list\")"));
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(batchInsertMethod);

        // add method `batchInsertSelective`
        Method batchInsertSelectiveMethod = new Method();
        batchInsertSelectiveMethod.setVisibility(JavaVisibility.PUBLIC);
        batchInsertSelectiveMethod.setReturnType(returnType);
        batchInsertSelectiveMethod.setName("batchInsertSelective");
        FullyQualifiedJavaType paramTypeSelective = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType paramListTypeSelective = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        paramTypeSelective.addTypeArgument(paramListTypeSelective);
        batchInsertSelectiveMethod.addParameter(new Parameter(paramTypeSelective, "list", "@Param(\"list\")"));
        batchInsertSelectiveMethod.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "columns", "@Param(\"columns\")", true));
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(batchInsertSelectiveMethod);
    }

    /**
     * Update Mapper.xml
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        addBatchInsertionXml(document, introspectedTable);
        addSelectiveBatchInsertionXml(document, introspectedTable);
        return true;
    }

    private void addBatchInsertionXml(Document document, IntrospectedTable introspectedTable) {
        XmlElement insertBatchElement = new XmlElement("insert");
        insertBatchElement.addAttribute(new Attribute("id", "batchInsert"));
        // 批量插入数据同步返回数据的自增 id，需要 mybatis 版本号不能低于 3.3.1
        insertBatchElement.addAttribute(new Attribute("useGeneratedKeys", "true"));
        insertBatchElement.addAttribute(new Attribute("keyProperty", "id"));
        insertBatchElement.addAttribute(new Attribute("parameterType", "java.util.List"));
        XmlElement valueTrimElement = new XmlElement("trim");
        valueTrimElement.addAttribute(new Attribute("prefix", " ("));
        valueTrimElement.addAttribute(new Attribute("suffix", ")"));
        valueTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        XmlElement columnTrimElement = new XmlElement("trim");
        columnTrimElement.addAttribute(new Attribute("prefix", "("));
        columnTrimElement.addAttribute(new Attribute("suffix", ")"));
        columnTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : columns) {
            String columnName = introspectedColumn.getActualColumnName();
            columnTrimElement.addElement(new TextElement(columnName+","));
            valueTrimElement.addElement(
                    new TextElement("#{item."
                            + introspectedColumn.getJavaProperty()
                            + ",jdbcType="
                            + introspectedColumn.getJdbcTypeName()
                            + "},"));
        }
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "index"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        insertBatchElement.addElement(new TextElement("insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        insertBatchElement.addElement(columnTrimElement);
        insertBatchElement.addElement(new TextElement(" values "));
        foreachElement.addElement(valueTrimElement);
        insertBatchElement.addElement(foreachElement);
        document.getRootElement().addElement(insertBatchElement);
    }

    private void addSelectiveBatchInsertionXml(Document document, IntrospectedTable introspectedTable) {
        XmlElement insertBatchElement = new XmlElement("insert");
        insertBatchElement.addAttribute(new Attribute("id", "batchInsertSelective"));
        insertBatchElement.addAttribute(new Attribute("parameterType", "map"));
        insertBatchElement.addAttribute(new Attribute("useGeneratedKeys", "true"));
        insertBatchElement.addAttribute(new Attribute("keyProperty", "id"));
        XmlElement foreachColumn = new XmlElement("foreach");
        foreachColumn.addAttribute(new Attribute("collection", "columns"));
        foreachColumn.addAttribute(new Attribute("index", "index"));
        foreachColumn.addAttribute(new Attribute("item", "item"));
        foreachColumn.addAttribute(new Attribute("separator", ","));
        foreachColumn.addAttribute(new Attribute("open", "("));
        foreachColumn.addAttribute(new Attribute("close", ")"));
        foreachColumn.addElement(new TextElement("${item}"));
        insertBatchElement.addElement(
                new TextElement(
                        "insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        insertBatchElement.addElement(foreachColumn);
        insertBatchElement.addElement(new TextElement(" values "));
        XmlElement valueTrimElement = new XmlElement("trim");
        valueTrimElement.addAttribute(new Attribute("prefix", " ("));
        valueTrimElement.addAttribute(new Attribute("suffix", ")"));
        valueTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        XmlElement foreachColumnForValue = new XmlElement("foreach");
        foreachColumnForValue.addAttribute(new Attribute("collection", "columns"));
        foreachColumnForValue.addAttribute(new Attribute("index", "index"));
        foreachColumnForValue.addAttribute(new Attribute("item", "column"));
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : columns) {
            String columnName = introspectedColumn.getActualColumnName();
            XmlElement check = new XmlElement("if");
            check.addAttribute(new Attribute("test", "'" + columnName + "' == column"));
            check.addElement(
                    new TextElement("#{record."
                            + introspectedColumn.getJavaProperty()
                            + ",jdbcType="
                            + introspectedColumn.getJdbcTypeName()
                            + "},"));
            foreachColumnForValue.addElement(check);
        }
        valueTrimElement.addElement(foreachColumnForValue);
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "index"));
        foreachElement.addAttribute(new Attribute("item", "record"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addElement(valueTrimElement);
        insertBatchElement.addElement(foreachElement);
        document.getRootElement().addElement(insertBatchElement);
    }

}
