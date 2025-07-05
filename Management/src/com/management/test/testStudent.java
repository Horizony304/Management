package com.management.test;

import com.management.dao.student.StuDAO;
import com.management.entities.StudentEntity;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class testStudent
{
    @Test
    public void testDAO() throws Exception
    {
        StuDAO stuDAO = new StuDAO();
        /*StudentEntity student = new StudentEntity(null, "20210201", "洪志昊", "女", LocalDate.parse("2012-03-24"), "120324", 2);
        StudentEntity student2 = new StudentEntity(null, "20210301", "洪小花", "女", LocalDate.parse("2012-09-30"), "qwerwhy6", 3);
        int insert = stuDAO.insert(student);
        System.out.println("insert = " + insert);
        int insert1 = stuDAO.insert(student2);
        System.out.println("insert1 = " + insert1);*/
        /*StudentEntity student = new StudentEntity(null, "20210202", "洪志远", "男", LocalDate.parse("2003-03-04"), "hzy0304.", 2);
        int insert = stuDAO.insert(student);
        System.out.println("insert = " + insert);*/
        /*int delete = stuDAO.delete(1);
        System.out.println("delete = " + delete);*/
        /*StudentEntity student = new StudentEntity(null, "20210201", "洪小花", "男", LocalDate.parse("2012-03-24"), "337845818", 2);
        int modify = stuDAO.modify(student, 2);
        System.out.println("modify = " + modify);*/
        for (StudentEntity student : stuDAO.selectAll())
        {
            System.out.println("student = " + student);
        }
        StudentEntity student = stuDAO.selectByID(2);
        System.out.println("student = " + student);
    }

    @Test
    public void testtoString() throws Exception
    {
        /*StudentEntity student = new StudentEntity(5, "2021", "洪志远", "男", LocalDate.parse("2003-03-04"), "hzy", 3);
        System.out.println(student);*/
        System.out.println("======= 第 " + 1 + " 页，共 " + 5 + " 页 =======");
        List<StudentEntity> list = new StuDAO().selectByPage(1);
        int len = list.size();
        for (int i = 1; i <= len; i++)
        {
            System.out.println("学生-" + i);
            System.out.println(list.get(i - 1));
            if (i != len)
                System.out.println("------------------------------");
        }
    }


}
