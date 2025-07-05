package com.management.dao.student;

import com.management.entities.GradeEntity;
import com.management.entities.StudentEntity;
import com.management.dao.BaseDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实现Student学生的CRUD
 * 增删改
 * 按ID查询，按学号查询，按姓名查询，按性别查询，按班级ID查询，查询所有学号
 */
public class StuDAO extends BaseDAO
{
    /**
     * 增加一条学生数据
     * @param student 待增加的学生
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(StudentEntity student) throws SQLException
    {
        String sql = "insert into student_info (stu_id, stu_name, stu_gender, stu_birth, stu_pwd, cla_id) values (?, ?, ?, ?, ?, ?)";
        return insertWithKey(sql, student.getStuId(), student.getStuName(), student.getStuGender(), student.getStuBirth(), student.getStuPwd(), student.getClaId());
    }

    /**
     * 删除一条学生数据
     * @param id 待删除的学生ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from student_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条学生数据
     * @param student 修改后的学生数据
     * @param id 待修改的学生ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(StudentEntity student, int id) throws SQLException
    {
        String sql = "update student_info set stu_id = ?, stu_name = ?, stu_gender = ?, stu_birth = ?, stu_pwd = ?, cla_id = ? where id = ?";
        return update(sql, student.getStuId(), student.getStuName(), student.getStuGender(), student.getStuBirth(), student.getStuPwd(), student.getClaId(), id);
    }

    /**
     * 查询所有学生
     * @return 所有学生的列表
     * @throws Exception 异常
     */
    public List<StudentEntity> selectAll() throws Exception
    {
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info";
        return query(StudentEntity.class, sql);
    }

    /**
     * 查询学生表页数
     * @return 学生表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("student_info") / 10);
    }

    /**
     * 查询所有学生(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的学生列表
     * @throws Exception 异常
     */
    public List<StudentEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info limit 10 offset ?";
        return query(StudentEntity.class, sql, offset);
    }

    /**
     * 按ID查询一条学生数据
     * @param id 待查询的学生ID
     * @return 查询到的学生
     * @throws Exception 异常
     */
    public StudentEntity selectByID(int id) throws Exception
    {
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info where id = ?";
        return querySingle(StudentEntity.class, sql, id);
    }

    /**
     * 按学号查询一条学生数据
     * @param id 待查询的学生学号
     * @return 查询到的学生
     * @throws Exception 异常
     */
    public StudentEntity selectByStu(String id) throws Exception
    {
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info where stu_id = ?";
        return querySingle(StudentEntity.class, sql, id);
    }

    /**
     * 按姓名查询学生数据
     * @param name 待查询的学生姓名
     * @return 查询到的学生列表
     * @throws Exception 异常
     */
    public List<StudentEntity> selectByName(String name) throws Exception
    {
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info where stu_name = ?";
        return query(StudentEntity.class, sql, name);
    }

    /**
     * 按性别查询学生数据
     * @param gender 待查询的性别
     * @return 查询到的学生列表
     * @throws Exception 异常
     */
    public List<StudentEntity> selectByGen(String gender) throws Exception
    {
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info where stu_gender = ?";
        return query(StudentEntity.class, sql, gender);
    }

    /**
     * 按班级ID查询学生数据
     * @param id 待查询的班级ID
     * @return 查询到的学生列表
     * @throws Exception 异常
     */
    public List<StudentEntity> selectByCla(int id) throws Exception
    {
        String sql = "select id, stu_id stuId, stu_name stuName, stu_gender stuGender, stu_birth stuBirth, stu_pwd stuPwd, cla_id claId from student_info where cla_id = ?";
        return query(StudentEntity.class, sql, id);
    }

    /**
     * 查询所有学号
     * @return 查询到的学号集合
     * @throws Exception 异常
     */
    public Set<String> selectAllStu() throws Exception
    {
        String sql = "select stu_id from student_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有ID
     * @return 查询到的ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllID() throws Exception
    {
        String sql = "select id from student_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有姓名
     * @return 查询到的姓名集合
     * @throws Exception 异常
     */
    public Set<String> selectAllName() throws Exception
    {
        String sql = "select distinct stu_name from student_info";
        return querySingleCol(sql);
    }

    /**
     * 按教师ID查询他的学生集合，列表转集合去重再转列表以调用printList(转集合去重需要重写equals/hashCode) -> 直接用distinct(作用于所有选中的列，而非单独某一列，只有当两行的所有列值都相同时才会被视为重复行)
     * @param teaId 待查询的教师ID
     * @return 学生集合
     * @throws Exception 异常
     */
    public List<StudentEntity> selectByTea(int teaId) throws Exception
    {
        String sql = "select distinct s.id id, s.stu_id stuId, s.stu_name stuName, s.stu_gender stuGender, s.stu_birth stuBirth, s.stu_pwd stuPwd, s.cla_id claId from student_info s, grade_info g, teach_info t where s.id = g.stu_id and g.teach_id = t.id and t.tea_id = ?";
        return query(StudentEntity.class, sql, teaId);
    }
}