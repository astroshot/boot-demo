package com.boot.mybatis.generator.plugin;

import com.boot.mybatis.generator.plugin.adapter.CustomPluginAdapter;

import java.util.List;

public class InsertOnDuplicateKeyPlugin extends CustomPluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

}
