package com.management.test;

import com.management.entities.AdminEntity;
import com.management.dao.admin.AdminDAO;
import com.management.service.admin.AdminService;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class testAdmin
{
    @Test
    public void testinsert() throws SQLException
    {
        AdminDAO adminDAO = new AdminDAO();
        AdminEntity admin = new AdminEntity(null, "hzhhh", "1230324");
        int result = adminDAO.insert(admin);
        if (result > 0)
            System.out.println("插入成功！ID值为" + result);
        else
            System.out.println("插入失败！");
    }

    @Test
    public void testdelete() throws SQLException
    {
        AdminDAO adminDAO = new AdminDAO();
        //int result = adminDAO.delete(1);
        int result = adminDAO.delete(2);
        if (result > 0)
            System.out.println("删除成功！");
        else
            System.out.println("删除失败！");
    }

    @Test
    public void testmodify() throws SQLException
    {
        AdminDAO adminDAO = new AdminDAO();
        AdminEntity admin = new AdminEntity(null, "hzyyy", "6664666");
        int result = adminDAO.modify(admin, 3);
        if (result > 0)
            System.out.println("修改成功！");
        else
            System.out.println("修改失败！");
    }

    @Test
    public void testSelectAll() throws Exception
    {
        AdminDAO adminDAO = new AdminDAO();
        List<AdminEntity> admins = adminDAO.selectAll();
        for (AdminEntity admin : admins)
        {
            System.out.println(admin);
        }
    }

    @Test
    public void testService()
    {
        AdminService adminService = new AdminService();
    }
}
