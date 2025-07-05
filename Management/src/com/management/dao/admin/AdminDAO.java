package com.management.dao.admin;

import com.management.entities.AdminEntity;
import com.management.dao.BaseDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * 实现Admin管理员的CRUD
 * 增删改
 * 查询所有，查询用户名
 */
public class AdminDAO extends BaseDAO
{
    /**
     * 增加一条管理员数据
     * @param admin 待增加的管理员
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(AdminEntity admin) throws SQLException
    {
        String sql = "insert into admin_info (ad_uname, ad_pwd) values (?, ?)";
        return insertWithKey(sql, admin.getAdUname(), admin.getAdPwd());
    }

    /**
     * 删除一条管理员数据
     * @param id 待删除的管理员ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from admin_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条管理员数据
     * @param admin 修改后的管理员数据
     * @param id 待修改的管理员ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(AdminEntity admin, int id) throws SQLException
    {
        String sql = "update admin_info set ad_uname = ?, ad_pwd = ? where id = ?";
        return update(sql, admin.getAdUname(), admin.getAdPwd(), id);
    }

    /**
     * 查询所有管理员
     * @return 所有管理员的列表
     * @throws Exception 异常
     */
    public List<AdminEntity> selectAll() throws Exception
    {
        String sql = "select id, ad_uname adUname, ad_pwd adPwd from admin_info";
        return query(AdminEntity.class, sql);
    }

    /**
     * 按用户名查询一条管理员
     * @param name 待查询的用户名
     * @return 查询到的管理员
     * @throws Exception 异常
     */
    public AdminEntity selectByName(String name) throws Exception
    {
        String sql = "select id, ad_uname adUname, ad_pwd adPwd from admin_info where ad_uname = ?";
        return querySingle(AdminEntity.class, sql, name);
    }
}