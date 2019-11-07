package com.boot.mybatis.generator.introspected;

import com.boot.mybatis.generator.config.AssociationConfig;
import com.boot.mybatis.generator.config.TableConfig;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;

import java.util.List;

public class CustomIntrospectedTableMybatis3Impl extends IntrospectedTableMyBatis3SimpleImpl {

    protected TableConfig tableConfig;

    protected List<AssociationConfig> associationConfigList;

    protected enum CustomInternalAttribute {
        ATTR_RESULT_MAP_WITH_ASSOCIATIONS_ID,
        ATTR_SELECT_BY_PRIMARY_KEY_WITH_ASSOCIATIONS_STATEMENT_ID;
    }

    // TODO
    public String getSelectByPrimaryKeyWithAssociationsStatementId() {
        return internalAttributes
                .get(CustomInternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_WITH_ASSOCIATIONS_STATEMENT_ID);
    }

    // TODO
    public String getResultMapWithAssociationsId() {
        return internalAttributes
                .get(CustomInternalAttribute.ATTR_RESULT_MAP_WITH_ASSOCIATIONS_ID);
    }

    // 不生成 BLOB 相关代码
    @Override
    public void addColumn(IntrospectedColumn introspectedColumn) {
        baseColumns.add(introspectedColumn);
        introspectedColumn.setIntrospectedTable(this);
    }

    public List<AssociationConfig> getAssociationColumns() {
        return associationConfigList;
    }

}
