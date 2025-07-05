package com.management.dao.grade;

import com.management.dao.BaseDAO;
import com.management.entities.GradeEntity;
import com.management.entities.StudentEntity;
import com.management.entities.TeachEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 实现Grade成绩的CRUD
 * 增删改
 * 按ID查询，按授课ID查询，按学生ID查询
 */
public class GradeDAO extends BaseDAO
{
    /**
     * 增加一条成绩数据
     * @param grade 待增加的成绩
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(GradeEntity grade) throws SQLException
    {
        String sql = "insert into grade_info (teach_id, stu_id, g_score) values (?, ?, ?)";
        return insertWithKey(sql, grade.getTeachId(), grade.getStuId(), grade.getGScore());
    }

    /**
     * 删除一条成绩数据
     * @param id 待删除的成绩ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from grade_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条成绩数据
     * @param grade 修改后的成绩数据
     * @param id 待修改的成绩ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(GradeEntity grade, int id) throws SQLException
    {
        String sql = "update grade_info set teach_id = ?, stu_id = ?, g_score = ? where id = ?";
        return update(sql, grade.getTeachId(), grade.getStuId(), grade.getGScore(), id);
    }

    /**
     * 查询所有成绩
     * @return 所有成绩的列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectAll() throws Exception
    {
        String sql = "select id, teach_id teachId, stu_id stuId, g_score gScore from grade_info";
        return query(GradeEntity.class, sql);
    }

    /**
     * 按ID查询一条成绩数据
     * @param id 待查询的成绩ID
     * @return 查询到的成绩
     */
    public GradeEntity selectByID(int id) throws Exception
    {
        String sql = "select id, teach_id teachId, stu_id stuId, g_score gScore from grade_info where id = ?";
        return querySingle(GradeEntity.class, sql, id);
    }

    /**
     * 按授课ID查询成绩数据
     * @param teachId 待查询的授课ID
     * @return 查询到的成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByTeach(int teachId) throws Exception
    {
        String sql = "select id, teach_id teachId, stu_id stuId, g_score gScore from grade_info where teach_id = ?";
        return query(GradeEntity.class, sql, teachId);
    }

    /**
     * 对列表里的每个授课ID查询对应的成绩
     * @param teaches 授课计划列表
     * @return 查询到的成绩列表
     */
    public List<GradeEntity> selectByTeach(List<TeachEntity> teaches) throws Exception
    {
        List<GradeEntity> result = new ArrayList<>();
        if (teaches.isEmpty())  //若teaches空
            return result;  //返回空列表
        for (TeachEntity teach : teaches)
        {
            List<GradeEntity> grades = selectByTeach(teach.getId());
            if (grades.isEmpty())
                continue;
            result.addAll(grades);  //批量添加
        }
        return result;
    }

    /**
     * 查询分数为空的成绩列表
     * @param teaches 待查询的授课计划列表
     * @return 查询到的成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByTeachNull(List<TeachEntity> teaches) throws Exception
    {
        List<GradeEntity> result = new ArrayList<>();
        if (teaches.isEmpty())
            return result;
        for (TeachEntity teach : teaches)
        {
            List<GradeEntity> grades = selectByTeach(teach.getId());
            if (grades.isEmpty())
                continue;
            for (GradeEntity grade : grades)
            {
                if (grade.getGScore() == null)  //未赋分
                    result.add(grade);
            }
        }
        return result;
    }

    /**
     * 查询分数不为空的成绩列表
     * @param teaches 待查询的授课计划列表
     * @return 查询到的成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByTeachNonNull(List<TeachEntity> teaches) throws Exception
    {
        List<GradeEntity> result = new ArrayList<>();
        if (teaches.isEmpty())
            return result;
        for (TeachEntity teach : teaches)
        {
            List<GradeEntity> grades = selectByTeach(teach.getId());
            if (grades.isEmpty())
                continue;
            for (GradeEntity grade : grades)
            {
                if (grade.getGScore() != null)  //已赋分
                    result.add(grade);
            }
        }
        return result;
    }

    /**
     * 按学生ID查询成绩数据
     * @param stuId 待查询的学生ID
     * @return 查询到的成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByStu(int stuId) throws Exception
    {
        String sql = "select g.id id, teach_id teachId, stu_id stuId, g_score gScore from grade_info g, teach_info t where g.teach_id = t.id and stu_id = ? order by t.teach_end desc";
        return query(GradeEntity.class, sql, stuId);
    }

    /**
     * 对学生列表查询成绩数据
     * @param students 待查询的学生列表
     * @return 查询到的成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByStu(List<StudentEntity> students) throws Exception
    {
        List<GradeEntity> result = new ArrayList<>();
        if (students.isEmpty())
            return result;  //返回空列表
        for (StudentEntity student : students)
        {
            List<GradeEntity> grades = selectByStu(student.getId());
            if (grades.isEmpty())
                continue;
            result.addAll(grades);
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
        String sql = "select id from grade_info";
        return querySingleCol(sql);
    }

    /**
     * 查询选课与成绩表页数
     * @return 选课与成绩表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("grade_info") / 10);
    }

    /**
     * 查询所有选课与成绩(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的选课与成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, teach_id teachId, stu_id stuId, g_score gScore from teach_info limit 10 offset ?";
        return query(GradeEntity.class, sql, offset);
    }

    /**
     * 按教师ID和学生学号查询成绩，并按结课时间降序排列成列表
     * @param teaId 教师ID
     * @param stuId 学生学号
     * @return 查询到的成绩列表
     * @throws Exception 异常
     */
    public List<GradeEntity> selectByStu(int teaId, String stuId) throws Exception
    {
        String sql = "select g.id id, g.teach_id teachId, g.stu_id stuId, g.g_score gScore from grade_info g, teach_info t, student_info s where g.stu_id = s.id and g.teach_id = t.id and t.tea_id = ? and s.stu_id = ? order by t.teach_end desc";
        return query(GradeEntity.class, sql, teaId, stuId);
    }

    public GradeEntity selectByTeachStu(int teachId, int stuId) throws Exception
    {
        String sql = "select id, teach_id teachId, stu_id stuId, g_score gScore from grade_info where teach_id = ? and stu_id = ?";
        return querySingle(GradeEntity.class, sql, teachId, stuId);
    }
}