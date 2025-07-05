package com.management.dao.teacher;

import com.management.dao.BaseDAO;
import com.management.dao.course.CourseDAO;
import com.management.dao.teach.TeachDAO;
import com.management.entities.CourseEntity;
import com.management.entities.StudentEntity;
import com.management.entities.TeachEntity;
import com.management.entities.TeacherEntity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 实现Teacher老师的CRUD
 * 增删改
 * 按ID查询，按姓名查询，按性别查询
 */
public class TeaDAO extends BaseDAO
{
    /**
     * 增加一条教师数据
     * @param teacher 待增加的老师
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(TeacherEntity teacher) throws SQLException
    {
        String sql = "insert into teacher_info (tea_name, tea_gender, tea_pwd) values (?, ?, ?)";
        return insertWithKey(sql, teacher.getTeaName(), teacher.getTeaGender(), teacher.getTeaPwd());
    }

    /**
     * 删除一条教师数据
     * @param id 待删除的教师ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from teacher_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条教师数据
     * @param teacher 修改后的教师数据
     * @param id 待修改的教师ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(TeacherEntity teacher, int id) throws SQLException
    {
        String sql = "update teacher_info set tea_name = ?, tea_gender = ?, tea_pwd = ? where id = ?";
        return update(sql, teacher.getTeaName(), teacher.getTeaGender(), teacher.getTeaPwd(), id);
    }

    /**
     * 查询所有教师
     * @return 所有教师的列表
     * @throws Exception 异常
     */
    public List<TeacherEntity> selectAll() throws Exception
    {
        String sql = "select id, tea_name teaName, tea_gender teaGender, tea_pwd teaPwd from teacher_info";
        return query(TeacherEntity.class, sql);
    }

    /**
     * 按ID查询一条教师数据
     * @param id 待查询的教师ID
     * @return 查询到的教师
     * @throws Exception 异常
     */
    public TeacherEntity selectByID(int id) throws Exception
    {
        String sql = "select id, tea_name teaName, tea_gender teaGender, tea_pwd teaPwd from teacher_info where id = ?";
        return querySingle(TeacherEntity.class, sql, id);
    }

    /**
     * 按姓名查询教师数据
     * @param name 待查询的教师姓名
     * @return 查询到的教师
     * @throws Exception 异常
     */
    public List<TeacherEntity> selectByName(String name) throws Exception
    {
        String sql = "select id, tea_name teaName, tea_gender teaGender, tea_pwd teaPwd from teacher_info where tea_name = ?";
        return query(TeacherEntity.class, sql, name);
    }

    /**
     * 按性别查询教师数据
     * @param gender 待查询的教师性别
     * @return 查询到的教师
     * @throws Exception 异常
     */
    public List<TeacherEntity> selectByGen(String gender) throws Exception
    {
        String sql = "select id, tea_name teaName, tea_gender teaGender, tea_pwd teaPwd from teacher_info where tea_gender = ?";
        return query(TeacherEntity.class, sql, gender);
    }

    /**
     * 查询所有ID
     * @return 查询到的ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllID() throws Exception
    {
        String sql = "select id from teacher_info";
        return querySingleCol(sql);
    }

    /**
     * 查询教师表页数
     * @return 教师表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("teacher_info") / 10);
    }

    /**
     * 查询所有教师(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的教师列表
     * @throws Exception 异常
     */
    public List<TeacherEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, tea_name teaName, tea_gender teaGender, tea_pwd teaPwd from teacher_info limit 10 offset ?";
        return query(TeacherEntity.class, sql, offset);
    }

    /**
     * 查询所有姓名
     * @return 查询到的姓名集合
     * @throws Exception 异常
     */
    public Set<String> selectAllName() throws Exception
    {
        String sql = "select distinct tea_name from teacher_info";
        return querySingleCol(sql);
    }


}