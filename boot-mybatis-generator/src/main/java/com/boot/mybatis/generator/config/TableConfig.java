package com.boot.mybatis.generator.config;

import java.util.List;

public enum TableConfig {

    ADV_UNIT_STATE("adv_unit_state", 8),
    ADV_UNIT_MATER("adv_unit_mater", 8);


    private String tableName;

    private int splitNumber;

    private List<AssociationConfig> associationConfigs;

    TableConfig(String tableName, int splitNumber) {
        this.tableName = tableName;
        this.splitNumber = splitNumber;
    }

    public String getTableName() {
        return tableName;
    }

    public int getSplitNumber() {
        return splitNumber;
    }

    public List<AssociationConfig> getAssociationConfigs() {
        return associationConfigs;
    }

    public static TableConfig getByTableName(String tableName) {

        for (TableConfig tableConfig : values()) {
            if (tableConfig.tableName.equalsIgnoreCase(tableName)) {
                return tableConfig;
            }
        }

        return null;
    }
}
