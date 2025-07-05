package com.management.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC工具类：
 *      1.维护一个Druid连接池对象，维护一个线程绑定连接的ThreadLocal对象
 *      2.对外提供在ThreadLocal中获取连接的方法
 *      3.对外提供回收连接的方法，且将连接对象从ThreadLocal中归还给Druid连接池
 */
public class JDBCUtil
{
    //整个应用共享同一个DataSource和ThreadLocal实例
    private static DataSource dataSource;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private JDBCUtil()
    {
        throw new AssertionError("工具类不可实例化！");
    }

    //类加载时即创建连接池对象，赋值给dataSource
    static
    {
        try
        {
            Properties properties = new Properties();
            InputStream inputStream = JDBCUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            properties.load(inputStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    //在连接池中获取连接
    public static Connection getConnection() throws SQLException
    {
        Connection connection = threadLocal.get();
        if (connection == null)
        {
            connection = dataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }

    //将连接归还给连接池
    public static void releaseConn() throws SQLException
    {
        Connection connection = threadLocal.get();
        if (connection != null)
        {
            threadLocal.remove();
            connection.setAutoCommit(true); //开启自动提交
            connection.close();
        }
    }
}