package com.management;

import com.management.utils.SystemUtil;

import java.io.IOException;

public class Main
{
    //系统入口
    public static void main(String[] args) throws IOException
    {
        // 指定日志配置文件(为了不再自动输出Druid连接池初始化信息)
        System.setProperty("java.util.logging.config.file", "src/resources/logging.properties");
        SystemUtil.systemEntry();
    }
}