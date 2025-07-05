package com.management.test;

import com.management.dao.BaseDAO;
import com.management.dao.course.CourseDAO;
import com.management.dao.grade.GradeDAO;
import com.management.dao.student.StuDAO;
import com.management.dao.teach.TeachDAO;
import com.management.dao.teacou.TeaCouDAO;
import com.management.entities.*;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class testDAO
{
    @Test
    public void testBaseTotal() throws SQLException
    {
        BaseDAO baseDAO = new BaseDAO();
        int count = baseDAO.totalCount("class_info");
        System.out.println("count = " + count);
    }

    @Test
    public void testGradeDAO() throws Exception
    {
        List<GradeEntity> grades = new GradeDAO().selectByStu(1);
        /*if (grades == null)
            System.out.println("空！");*/
        if (grades.size() == 0)
            System.out.println("空！");
    }

    @Test
    public void testDAO() throws Exception
    {
        GradeDAO gradeDAO = new GradeDAO();
        GradeEntity grade = new GradeEntity(null, 9, 6, null);
        int insert = gradeDAO.insert(grade);
        if (insert > 0)
            System.out.println("插入成功！");
    }
}
