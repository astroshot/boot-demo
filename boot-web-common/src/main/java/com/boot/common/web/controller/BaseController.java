package com.boot.common.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class BaseController {

    /**
     * base logger for all controllers
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * springMVC 框架解析 list 型参数时，默认只接收 256 个数据
     *
     * @param binder default param
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(10000);
    }

}
