package com.management.dao.teach;

import com.management.dao.BaseDAO;
import com.management.dao.course.CourseDAO;
import com.management.dao.grade.GradeDAO;
import com.management.entities.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 实现Teach授课计划的CRUD
 * 增删改
 * 按ID查询，按教师ID查询，按课程ID查询，按开始时间查询，按结束时间查询
 */
public class TeachDAO extends BaseDAO
{
    /**
     * 增加一条授课数据
     * @param teach 待增加的授课
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(TeachEntity teach) throws SQLException
    {
        String sql = "insert into teach_info (tea_id, cou_id, teach_start, teach_end) values (?, ?, ?, ?)";
        return insertWithKey(sql, teach.getTeaId(), teach.getCouId(), teach.getTeachStart(), teach.getTeachEnd());
    }

    /**
     * 删除一条授课数据
     * @param id 待删除的授课ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from teach_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条授课数据
     * @param teach 修改后的授课数据
     * @param id 待修改的授课ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(TeachEntity teach, int id) throws SQLException
    {
        String sql = "update teach_info set tea_id = ?, cou_id = ?, teach_start = ?, teach_end = ? where id = ?";
        return update(sql, teach.getTeaId(), teach.getCouId(), teach.getTeachStart(), teach.getTeachEnd(), id);
    }

    /**
     * 查询所有授课
     * @return 所有授课的列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectAll() throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info order by teach_end desc";
        return query(TeachEntity.class, sql);
    }

    /**
     * 按ID查询一条授课数据
     * @param id 待查询的授课ID
     * @return 查询到的授课
     * @throws Exception 异常
     */
    public TeachEntity selectByID(int id) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where id = ? ";
        return querySingle(TeachEntity.class, sql ,id);
    }

    /**
     * 按教师ID查询授课数据
     * @param teaId 待查询的教师ID
     * @return 查询到的授课列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectByTea(int teaId) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where tea_id = ? order by teach_end desc";
        return query(TeachEntity.class, sql, teaId);
    }

    /**
     * 按教师ID查询已结课的授课计划
     * @param teaId 待查询的教师ID
     * @return 授课计划列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectByTeaEnd(int teaId) throws Exception
    {
        List<TeachEntity> result = new ArrayList<>();
        List<TeachEntity> list = selectByTea(teaId);
        LocalDate currentDate = LocalDate.now();
        for (TeachEntity teach : list)
        {
            if (teach.getTeachEnd().isBefore(currentDate))  //已结课
                result.add(teach);
        }
        return result;
    }

    public List<TeachEntity> selectByCouName(String couName) throws Exception
    {
        TeachDAO teachDAO = new TeachDAO();
        CourseDAO courseDAO = new CourseDAO();
        CourseEntity course = courseDAO.selectByName(couName);
        List<TeachEntity> result = new ArrayList<>();
        List<TeachEntity> teaches = teachDAO.selectByCou(course.getId());
        LocalDate currentDate = LocalDate.now();
        for (TeachEntity teach : teaches)
        {
            if (teach.getTeachStart().isAfter(currentDate))
                result.add(teach);
        }
        return result;
    }

    public List<TeachEntity> selectByStu(int stuId) throws Exception
    {
        GradeDAO gradeDAO = new GradeDAO();
        TeachDAO teachDAO = new TeachDAO();
        List<TeachEntity> result = new ArrayList<>();
        List<GradeEntity> grades = gradeDAO.selectByStu(stuId);
        if (grades.isEmpty())
            return result;
        LocalDate currentDate = LocalDate.now();
        for (GradeEntity grade : grades)
        {
            TeachEntity teach = teachDAO.selectByID(grade.getTeachId());
            if (teach.getTeachStart().isAfter(currentDate))
                result.add(teach);
        }
        return result;
    }

    /**
     * 按课程ID查询授课数据
     * @param couId 待查询的课程ID
     * @return 查询到的授课列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectByCou(int couId) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where cou_id = ? order by teach_end desc";
        return query(TeachEntity.class, sql, couId);
    }

    /**
     * 按开始时间查询授课数据
     * @param date 待查询的开始时间
     * @return 查询到的授课列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectByStart(LocalDate date) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where teach_start = ?";
        return query(TeachEntity.class, sql, date);
    }

    /**
     * 按结束时间查询授课数据
     * @param date 待查询的结束时间
     * @return 查询到的授课列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectByEnd(LocalDate date) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where teach_end = ?";
        return query(TeachEntity.class, sql, date);
    }

    /**
     * 按教师ID、课程ID查询授课数据(真正的外键依赖，复合外键)
     * @param teaCou 待查询的教师-课程
     * @return 查询到的授课数据
     */
    public List<TeachEntity> selectByTeaCou(TeaCouEntity teaCou) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where (tea_id, cou_id) = (?, ?) order by teach_end desc";
        return query(TeachEntity.class, sql, teaCou.getTeaId(), teaCou.getCouId());
    }

    /**
     * 按教师-课程列表查询授课数据(真正的外键依赖，复合外键)
     * @param teaCous 教师-课程列表
     * @return 查询到的授课数据
     */
    public List<TeachEntity> selectByTeaCou(List<TeaCouEntity> teaCous) throws Exception
    {
        List<TeachEntity> result = new ArrayList<>();
        if (teaCous.isEmpty())
            return result;  //返回空列表
        for (TeaCouEntity teaCou : teaCous)
        {
            List<TeachEntity> teaches = selectByTeaCou(teaCou);
            if (teaches.isEmpty())
                continue;
            result.addAll(teaches); //批量添加
        }
        return result;
    }

    /**
     * 查询所有ID
     * @return 查询到的ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllID() throws Exception
    {
        String sql = "select id from teach_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有开课时间
     * @return 查询到的开课时间集合
     * @throws Exception 异常
     */
    public Set<LocalDate> selectAllStart() throws Exception
    {
        String sql = "select distinct teach_start from teach_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有结课时间
     * @return 查询到的结课时间集合
     * @throws Exception 异常
     */
    public Set<LocalDate> selectAllEnd() throws Exception
    {
        String sql = "select distinct teach_end from teach_info";
        return querySingleCol(sql);
    }

    /**
     * 查询授课计划表页数
     * @return 课计划表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("teach_info") / 10);
    }

    /**
     * 查询所有授课计划(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的授课计划列表
     * @throws Exception 异常
     */
    public List<TeachEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info order by teach_end desc limit 10 offset ?";
        return query(TeachEntity.class, sql, offset);
    }

    public TeachEntity selectByAll(int teaId, int couId, LocalDate start) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId, teach_start teachStart, teach_end teachEnd from teach_info where tea_id = ? and cou_id = ? and teach_start = ?";
        return querySingle(TeachEntity.class, sql, teaId, couId, start);
    }
}