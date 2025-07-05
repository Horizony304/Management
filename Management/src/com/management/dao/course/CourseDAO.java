package com.management.dao.course;

import com.management.dao.BaseDAO;
import com.management.entities.CourseEntity;
import com.management.entities.TeacherEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * 实现Course课程的CRUD
 * 增删改
 * 按ID查询，按课程名查询，按专业查询
 */
public class CourseDAO extends BaseDAO
{
    /**
     * 增加一条课程数据
     * @param course 待增加的课程
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(CourseEntity course) throws SQLException
    {
        String sql = "insert into course_info (cou_name, cou_major, cou_cre) values (?, ?, ?)";
        return insertWithKey(sql, course.getCouName(), course.getCouMajor(), course.getCouCre());
    }

    /**
     * 删除一条课程数据
     * @param id 待删除的课程ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from course_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条课程数据
     * @param course 修改后的课程数据
     * @param id 待修改的课程ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(CourseEntity course, int id) throws SQLException
    {
        String sql = "update course_info set cou_name = ?, cou_major = ?, cou_cre = ? where id = ?";
        return update(sql, course.getCouName(), course.getCouMajor(), course.getCouCre(), id);
    }

    /**
     * 查询所有课程
     * @return 所有课程的列表
     * @throws Exception 异常
     */
    public List<CourseEntity> selectAll() throws Exception
    {
        String sql = "select id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info";
        return query(CourseEntity.class, sql);
    }

    /**
     * 按ID查询一条课程数据
     * @param id 待查询的课程ID
     * @return 查询到的课程
     * @throws Exception 异常
     */
    public CourseEntity selectByID(int id) throws Exception
    {
        String sql = "select id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info where id = ?";
        return querySingle(CourseEntity.class, sql, id);
    }

    /**
     * 按课程名查询一条课程数据
     * @param name 待查询的课程名
     * @return 查询到的课程
     * @throws Exception 异常
     */
    public CourseEntity selectByName(String name) throws Exception
    {
        String sql = "select id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info where cou_name = ?";
        return querySingle(CourseEntity.class, sql, name);
    }

    /**
     * 按专业查询课程数据
     * @param major 待查询的专业名
     * @return 查询到的课程
     * @throws Exception 异常
     */
    public List<CourseEntity> selectByMajor(String major) throws Exception
    {
        String sql = "select id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info where cou_major = ?";
        return query(CourseEntity.class, sql, major);
    }

    /**
     * 查询所有课程ID
     * @return 查询到的课程ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllID() throws Exception
    {
        String sql = "select id from course_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有课程名
     * @return 查询到的课程名集合
     * @throws Exception 异常
     */
    public Set<String> selectAllName() throws Exception
    {
        String sql = "select cou_name from course_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有专业名
     * @return 查询到的专业名集合
     * @throws Exception 异常
     */
    public Set<String> selectAllMajor() throws Exception
    {
        String sql = "select distinct cou_major from course_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有学分
     * @return 查询到的学分集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllCre() throws Exception
    {
        String sql = "select distinct cou_cre from course_info";
        return querySingleCol(sql);
    }

    /**
     * 查询课程表页数
     * @return 课程表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("course_info") / 10);
    }

    /**
     * 查询所有课程(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的课程列表
     * @throws Exception 异常
     */
    public List<CourseEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info limit 10 offset ?";
        return query(CourseEntity.class, sql, offset);
    }

    /**
     * 按学分查询课程数据
     * @param couCre 待查询的学分
     * @return 查询到的课程
     * @throws Exception 异常
     */
    public List<CourseEntity> selectByCre(int couCre) throws Exception
    {
        String sql = "select id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info where cou_cre = ?";
        return query(CourseEntity.class, sql, couCre);
    }

    /**
     * 按教师ID查询可教的课程
     * @param teaId 待查询的教师ID
     * @return 查询到的课程列表
     * @throws Exception 异常
     */
    public List<CourseEntity> selectByTea(int teaId) throws Exception
    {
        String sql = "select c.id id, cou_name couName, cou_major couMajor, cou_cre couCre from course_info c, tea_cou_info tc where c.id = tc.cou_id and tea_id = ?";
        return query(CourseEntity.class, sql, teaId);
    }
}