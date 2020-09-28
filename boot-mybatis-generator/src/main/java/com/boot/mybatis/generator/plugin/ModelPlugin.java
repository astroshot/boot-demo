package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.enums.LombokAnnotation;
import com.boot.mybatis.generator.plugin.adapter.CustomPluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ModelPlugin extends CustomPluginAdapter {

    private final Collection<LombokAnnotation> annotations;

    public ModelPlugin() {
        annotations = new HashSet<LombokAnnotation>(LombokAnnotation.values().length);
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * Intercepts base record class generation
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass, true);
        addTableColumn(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * Intercepts primary key class generation
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass, false);
        addTableColumn(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass, true);
        addTableColumn(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * Prevents all getters from being generated.
     * See SimpleModelGenerator
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    /**
     * Prevents all setters from being generated
     * See SimpleModelGenerator
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    /**
     * Adds the lombok annotations' imports and annotations to the class
     */
    private void addDataAnnotation(TopLevelClass topLevelClass, boolean builder) {
        for (LombokAnnotation annotation : annotations) {
            if (!builder && annotation.getParamName().equals("builder")) {
                continue;
            }
            topLevelClass.addImportedType(annotation.getJavaType());
            topLevelClass.addAnnotation(annotation.getName());
        }
    }

    private void addTableColumn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        InnerEnum innerEnum = new InnerEnum(new FullyQualifiedJavaType("COLUMNS"));
        innerEnum.setVisibility(JavaVisibility.PUBLIC);
        innerEnum.setStatic(true);
        Field columnField = new Field("column", FullyQualifiedJavaType.getStringInstance());
        columnField.setVisibility(JavaVisibility.PRIVATE);
        columnField.addAnnotation(LombokAnnotation.GETTER.getName());
        columnField.setFinal(true);
        innerEnum.addField(columnField);
        introspectedTable.getAllColumns().forEach(introspectedColumn -> {
            String sb = introspectedColumn.getActualColumnName().toUpperCase() +
                    "(\"" +
                    introspectedColumn.getActualColumnName() +
                    "\")";
            innerEnum.addEnumConstant(sb);
        });
        innerEnum.addAnnotation(LombokAnnotation.ALL_ARGS_CONSTRUCTOR.getName());
        topLevelClass.addInnerEnum(innerEnum);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        // 要添加到类上的注解
        annotations.add(LombokAnnotation.DATA);
        annotations.add(LombokAnnotation.GETTER);
        annotations.add(LombokAnnotation.BUILDER);
        annotations.add(LombokAnnotation.ALL_ARGS_CONSTRUCTOR);
        // annotations.add(Annotations.NO_ARGS_CONSTRUCTOR);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            boolean isEnable = Boolean.parseBoolean(entry.getValue().toString());
            if (isEnable) {
                String paramName = entry.getKey().toString().trim();
                LombokAnnotation annotation = LombokAnnotation.getValueOf(paramName);
                if (annotation != null) {
                    annotations.add(annotation);
                    annotations.addAll(LombokAnnotation.getDependencies(annotation));
                }
            }
        }
    }
}
