package com.management.test;

import com.management.utils.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testUtils
{
    /**
     * 测试JDBC工具类实例化对象抛出的Error长什么样
     */
    @Test
    public void testJDBCUtil() throws Exception
    {
        Class<JDBCUtil> jdbcUtilClass = JDBCUtil.class;
        Constructor<JDBCUtil> declaredConstructor = jdbcUtilClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        JDBCUtil jdbcUtil = declaredConstructor.newInstance();
    }

    /**
     * 测试JDBC工具类能否正常获取连接和释放连接
     */
    @Test
    public void testJDBCUtil1() throws SQLException
    {
        Connection connection = JDBCUtil.getConnection();
        System.out.println("connection = " + connection);
        JDBCUtil.releaseConn();
        System.out.println("connection = " + connection);
    }
}
