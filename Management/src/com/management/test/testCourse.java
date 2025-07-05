package com.management.test;

import com.management.dao.course.CourseDAO;
import com.management.entities.CourseEntity;
import org.junit.Test;

import java.sql.SQLException;

public class testCourse
{
    @Test
    public void testDAO() throws Exception
    {
        CourseDAO courseDAO = new CourseDAO();
        CourseEntity cou1 = new CourseEntity(null, "长江黄河", "地理学", 2);
        /*CourseEntity cou2 = new CourseEntity(null, "操作系统", "计算机", 3);
        int i1 = courseDAO.insert(cou1);
        System.out.println("i1 = " + i1);
        int i2 = courseDAO.insert(cou2);
        System.out.println("i2 = " + i2);*/
        /*int modify = courseDAO.modify(cou1, 2);
        System.out.println("modify = " + modify);*/
        /*int delete = courseDAO.delete(1);
        System.out.println("delete = " + delete);*/
        int key = courseDAO.insert(cou1);
        System.out.println("key = " + key);
        for (CourseEntity courseEntity : courseDAO.selectAll())
        {
            System.out.println(courseEntity);
        }
        CourseEntity c = courseDAO.selectByID(2);
        System.out.println("c = " + c);

    }
}
