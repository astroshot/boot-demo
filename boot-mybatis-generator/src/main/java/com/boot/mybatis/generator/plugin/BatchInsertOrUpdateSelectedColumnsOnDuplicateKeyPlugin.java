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

public class BatchInsertOrUpdateSelectedColumnsOnDuplicateKeyPlugin extends EnhancedPluginAdapter {

    private static final String METHOD_NAME = "batchInsertOrUpdateSelectedColumnsOnDuplicateKey";

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addBatchInsertOnDuplicateKeyMethod(interfaze, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        addBatchInsertOnDuplicateKeyXml(document, introspectedTable);
        return true;
    }

    protected void addBatchInsertOnDuplicateKeyMethod(Interface interfaze, IntrospectedTable introspectedTable) {
        // import necessary classes
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
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
        FullyQualifiedJavaType paramTypeSelective = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType paramListTypeSelective = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        paramTypeSelective.addTypeArgument(paramListTypeSelective);
        insertOrUpdateOnDuplicateKeyMethod.addParameter(new Parameter(paramTypeSelective, "list", "@Param(\"list\")"));

        FullyQualifiedJavaType insertionColumnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType insertionColumnList = new FullyQualifiedJavaType(FullyQualifiedJavaType.getStringInstance().getFullyQualifiedName());
        insertionColumnType.addTypeArgument(insertionColumnList);

        insertOrUpdateOnDuplicateKeyMethod.addParameter(
                new Parameter(insertionColumnType, "insertionColumns", "@Param(\"insertionColumns\")"));
        insertOrUpdateOnDuplicateKeyMethod.addParameter(
                new Parameter(insertionColumnType, "updateColumns", "@Param(\"updateColumns\")"));
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(insertOrUpdateOnDuplicateKeyMethod);
    }

    protected void addBatchInsertOnDuplicateKeyXml(Document document, IntrospectedTable introspectedTable) {
        XmlElement batchInsertOrUpdateOnDuplicateKey = new XmlElement("insert");
        batchInsertOrUpdateOnDuplicateKey.addAttribute(new Attribute("id", METHOD_NAME));
        batchInsertOrUpdateOnDuplicateKey.addAttribute(new Attribute("useGeneratedKeys", "true"));
        batchInsertOrUpdateOnDuplicateKey.addAttribute(new Attribute("keyProperty", "id"));
        batchInsertOrUpdateOnDuplicateKey.addAttribute(new Attribute("parameterType", "map"));

        batchInsertOrUpdateOnDuplicateKey.addElement(
                new TextElement(
                        "insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement foreachInsertionColumn = new XmlElement("foreach");
        foreachInsertionColumn.addAttribute(new Attribute("collection", "insertionColumns"));
        foreachInsertionColumn.addAttribute(new Attribute("index", "index"));
        foreachInsertionColumn.addAttribute(new Attribute("item", "item"));
        foreachInsertionColumn.addAttribute(new Attribute("separator", ","));
        foreachInsertionColumn.addAttribute(new Attribute("open", "("));
        foreachInsertionColumn.addAttribute(new Attribute("close", ")"));
        foreachInsertionColumn.addElement(new TextElement("${item}"));

        batchInsertOrUpdateOnDuplicateKey.addElement(foreachInsertionColumn);

        batchInsertOrUpdateOnDuplicateKey.addElement(new TextElement(" values"));
        XmlElement valueTrimElement = new XmlElement("trim");
        valueTrimElement.addAttribute(new Attribute("prefix", " ("));
        valueTrimElement.addAttribute(new Attribute("suffix", ")"));
        valueTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "index"));
        foreachElement.addAttribute(new Attribute("item", "record"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addElement(valueTrimElement);

        XmlElement foreachColumnForValue = new XmlElement("foreach");
        foreachColumnForValue.addAttribute(new Attribute("collection", "insertionColumns"));
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
        batchInsertOrUpdateOnDuplicateKey.addElement(foreachElement);

        batchInsertOrUpdateOnDuplicateKey.addElement(new TextElement(" on duplicate key update "));
        XmlElement updateColumnTrimElement = new XmlElement("trim");
        updateColumnTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        XmlElement foreachUpdateColumn = new XmlElement("foreach");
        foreachUpdateColumn.addAttribute(new Attribute("collection", "updateColumns"));
        foreachUpdateColumn.addAttribute(new Attribute("index", "index"));
        foreachUpdateColumn.addAttribute(new Attribute("item", "column"));
        List<IntrospectedColumn> onUpdateColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : onUpdateColumns) {
            String columnName = introspectedColumn.getActualColumnName();
            XmlElement check = new XmlElement("if");
            check.addAttribute(new Attribute("test", "'" + columnName + "' == column"));
            check.addElement(
                    new TextElement("`" + columnName + "`"
                            + " = values(" + columnName + "), ")
            );
            foreachUpdateColumn.addElement(check);
        }
        updateColumnTrimElement.addElement(foreachUpdateColumn);
        batchInsertOrUpdateOnDuplicateKey.addElement(updateColumnTrimElement);
        document.getRootElement().addElement(batchInsertOrUpdateOnDuplicateKey);
    }
}
