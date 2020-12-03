package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.enums.SwaggerAnnotation;
import com.boot.mybatis.generator.plugin.adapter.CustomPluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class SwaggerAnnotationPlugin extends CustomPluginAdapter {

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
        addApiModelAnnotation(topLevelClass);
        return true;
    }

    /**
     * Intercepts primary key class generation
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addApiModelAnnotation(topLevelClass);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addApiModelAnnotation(topLevelClass);
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
                                              Plugin.ModelClassType modelClassType) {
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
                                              Plugin.ModelClassType modelClassType) {
        return false;
    }

    private void addApiModelAnnotation(TopLevelClass topLevelClass) {
        // Class Annotation format: @ApiModel
        topLevelClass.addImportedType(SwaggerAnnotation.API_MODEL.getJavaType());
        topLevelClass.addImportedType(SwaggerAnnotation.API_MODEL_PROPERTY.getJavaType());
        topLevelClass.addAnnotation(SwaggerAnnotation.API_MODEL.getName());
    }

    public void addFieldAnnotation(
            Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // Field Annotation format: @ApiModelProperty(value = "Comments from mysql schema")
        String annotation = String.format(
                "%s(value = \"%s\")", SwaggerAnnotation.API_MODEL_PROPERTY.getName(), introspectedColumn.getRemarks());
        field.addAnnotation(annotation);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
        addFieldAnnotation(field, introspectedTable, introspectedColumn);
        return true;
    }
}
