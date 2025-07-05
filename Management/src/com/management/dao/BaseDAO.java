package com.management.dao;

import com.management.utils.JDBCUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 将共性的数据库的操作代码封装在BaseDAO中
 */
public class BaseDAO
{
    private static final int pageSize = 10; //一页大小

    /**
     * 对数据库的删除和修改操作
     *
     * @param sql    待执行的SQL语句
     * @param params 待插入占位符的参数
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int update(String sql, Object... params) throws SQLException
    {
        Connection connection = JDBCUtil.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (params != null)
        {
            for (int i = 0; i < params.length; i++)
            {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }

        int rows = preparedStatement.executeUpdate();

        preparedStatement.close();
        JDBCUtil.releaseConn();
        return rows;
    }

    /**
     * 对数据库的带有主键回显效果的插入操作
     *
     * @param sql    待执行的SQL语句
     * @param params 待插入占位符的参数
     * @return 主键值或-1(插入失败)
     * @throws SQLException SQL异常
     */
    public int insertWithKey(String sql, Object... params) throws SQLException
    {
        Connection connection = JDBCUtil.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            if (params != null)
            {
                for (int i = 0; i < params.length; i++)
                {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            int result = preparedStatement.executeUpdate();
            if (result > 0) //插入成功
            {
                ResultSet generatedKey = preparedStatement.getGeneratedKeys();
                if (generatedKey.next())
                {
                    int key = generatedKey.getInt(1);
                    generatedKey.close();
                    return key;
                }
            } else            //插入失败
            {
                return -1;
            }
        } finally
        {
            JDBCUtil.releaseConn();
        }
        throw new AssertionError("Unreachable code");   //为了通过编译，编译器认为可能try块抛出异常此时会没有返回值
    }

    /**
     * 对数据库的查询操作(多行)
     *
     * @param clazz  数据类型的类对象
     * @param sql    待执行的SQL语句
     * @param params 待插入占位符的参数
     * @param <T>    数据类型
     * @return 查询结果集合
     * @throws Exception 异常
     */
    public <T> List<T> query(Class<T> clazz, String sql, Object... params) throws Exception
    {
        List<T> list = new ArrayList<>();//返回的集合
        Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);

        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (params != null)
        {
            for (int i = 0; i < params.length; i++)
            {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next())//存入集合
        {
            T t = declaredConstructor.newInstance();
            for (int i = 1; i <= columnCount; i++)
            {
                Object value = resultSet.getObject(i);

                //如果value是java.sql.Date类型，就手动转换为LocalDate类型
                if (value instanceof Date)
                    value = ((Date) value).toLocalDate();

                String columnLabel = metaData.getColumnLabel(i);
                Field field = clazz.getDeclaredField(columnLabel);
                field.setAccessible(true);
                field.set(t, value);
            }
            list.add(t);
        }

        resultSet.close();
        preparedStatement.close();
        JDBCUtil.releaseConn();
        return list;
    }

    /**
     * 对数据库的查询操作(单行)
     *
     * @param clazz  数据类型的类对象
     * @param sql    待执行的SQL语句
     * @param params 待插入占位符的参数
     * @param <T>    数据类型
     * @return 查询结果
     * @throws Exception 异常
     */
    public <T> T querySingle(Class<T> clazz, String sql, Object... params) throws Exception
    {
        List<T> list = this.query(clazz, sql, params);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

    /**
     * 对数据库的单列查询操作，用来比对是否存在
     * @param sql    待执行的SQL语句
     * @param params 待插入占位符的参数
     * @param <T>    数据类型
     * @return 查询结果
     * @throws Exception 异常
     */
    public <T> Set<T> querySingleCol(String sql, Object... params) throws Exception
    {
        Set<T> set = new HashSet<>();//返回的集合

        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (params != null)
        {
            for (int i = 0; i < params.length; i++)
            {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())//存入集合
        {
            Object value = resultSet.getObject(1);
            //如果value是java.sql.Date类型，就手动转换为LocalDate类型
            if (value instanceof Date)
                value = ((Date) value).toLocalDate();
            set.add((T) value);
        }

        resultSet.close();
        preparedStatement.close();
        JDBCUtil.releaseConn();
        return set;
    }

    /**
     * 计算某张表的数据数量，子类无需写SQL语句，传递表名即可
     *
     * @param tableName 表名
     * @return 该表的数据数量
     * @throws SQLException SQL异常
     */
    public int totalCount(String tableName) throws SQLException
    {
        String sql = "select count(*) from " + tableName;
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        int total = 0;
        if (resultSet.next())
            total = resultSet.getInt(1);
        return total;
    }
}