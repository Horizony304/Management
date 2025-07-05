package com.management.test;

import com.management.dao.clas.ClassDAO;
import com.management.entities.ClassEntity;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;

public class testClass
{
    @Test
    public void testinsert() throws SQLException
    {
        ClassDAO classDAO = new ClassDAO();
        ClassEntity classEntity = new ClassEntity(null, "21级新闻学1班", LocalDate.parse("2021-09-01"), LocalDate.parse("2025-06-30"));
        int key = classDAO.insert(classEntity);
        if (key > 0)
            System.out.println("插入成功！班级ID是" + key);
        else
            System.out.println("插入失败！");
    }

    @Test
    public void testdelete() throws SQLException
    {
        ClassDAO classDAO = new ClassDAO();
        int res = classDAO.delete(1);
    }

    @Test
    public void testmodify() throws SQLException
    {
        ClassDAO classDAO = new ClassDAO();
        ClassEntity classEntity = new ClassEntity(null, "21级计算机技术1班", LocalDate.parse("2025-09-01"), LocalDate.parse("2028-06-30"));
        int res = classDAO.modify(classEntity, 2);
    }

    @Test
    public void testselectAll() throws Exception
    {
        ClassDAO classDAO = new ClassDAO();
        for (ClassEntity classEntity : classDAO.selectAll())
        {
            System.out.println(classEntity);
        }
    }

    @Test
    public void testselectByID() throws Exception
    {
        ClassDAO classDAO = new ClassDAO();
        ClassEntity classEntity = classDAO.selectByID(3);
        System.out.println("classEntity = " + classEntity);
    }
}
