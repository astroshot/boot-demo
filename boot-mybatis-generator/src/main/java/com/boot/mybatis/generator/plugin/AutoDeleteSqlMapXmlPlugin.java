package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.plugin.adapter.EnhancedPluginAdapter;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.io.File;
import java.util.List;

public class AutoDeleteSqlMapXmlPlugin extends EnhancedPluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        String sqlMapPath = sqlMap.getTargetProject()
                + File.separator
                + sqlMap.getTargetPackage().replaceAll("\\.", File.separator)
                + File.separator + sqlMap.getFileName();
        File sqlMapFile = new File(sqlMapPath);
        try {
            boolean res = sqlMapFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
