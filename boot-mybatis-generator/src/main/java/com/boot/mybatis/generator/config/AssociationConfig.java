package com.boot.mybatis.generator.config;

public class AssociationConfig {

    private String column;

    private String property;

    private Class<?> clazz;

    private String select;

    public AssociationConfig(String column, String property, Class<?> clazz, String select) {
        this.column = column;
        this.property = property;
        this.clazz = clazz;
        this.select = select;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }
}
