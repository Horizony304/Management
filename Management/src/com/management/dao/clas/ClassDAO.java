package com.management.dao.clas;

import com.management.dao.BaseDAO;
import com.management.entities.ClassEntity;
import com.management.entities.TeacherEntity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 实现Class班级的CRUD
 * 增删改
 * 按ID查询，按班级名查询，按入学时间查询，按毕业时间查询，查询所有班级ID
 */
public class ClassDAO extends BaseDAO
{
    /**
     * 增加一条班级数据
     * @param clas 待增加的班级
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(ClassEntity clas) throws SQLException
    {
        String sql = "insert into class_info (cla_name, cla_start, cla_end) values (?, ?, ?)";
        return insertWithKey(sql, clas.getClaName(), clas.getClaStart(), clas.getClaEnd());
    }

    /**
     * 删除一条班级数据
     * @param id 待删除的班级ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from class_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条班级数据
     * @param clas 修改后的班级数据
     * @param id 待修改的班级ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(ClassEntity clas, int id) throws SQLException
    {
        String sql = "update class_info set cla_name = ?, cla_start = ?, cla_end = ? where id = ?";
        return update(sql, clas.getClaName(), clas.getClaStart(), clas.getClaEnd(), id);
    }

    /**
     * 查询所有班级
     * @return 所有班级的列表
     * @throws Exception 异常
     */
    public List<ClassEntity> selectAll() throws Exception
    {
        String sql = "select id, cla_name claName, cla_start claStart, cla_end claEnd from class_info order by cla_end desc";
        return query(ClassEntity.class, sql);
    }

    /**
     * 按ID查询一条班级数据
     * @param id 待查询的班级ID
     * @return 查询到的班级
     * @throws Exception 异常
     */
    public ClassEntity selectByID(int id) throws Exception
    {
        String sql = "select id, cla_name claName, cla_start claStart, cla_end claEnd from class_info where id = ?";
        return querySingle(ClassEntity.class, sql, id);
    }

    /**
     * 按班级名查询一条班级数据
     * @param name 待查询的班级名
     * @return 查询到的班级
     * @throws Exception 异常
     */
    public ClassEntity selectByName(String name) throws Exception
    {
        String sql = "select id, cla_name claName, cla_start claStart, cla_end claEnd from class_info where cla_name = ?";
        return querySingle(ClassEntity.class, sql, name);
    }

    /**
     * 按入学时间查询班级数据
     * @param date 待查询的入学时间
     * @return 查询到的班级列表
     * @throws Exception 异常
     */
    public List<ClassEntity> selectByStart(LocalDate date) throws Exception
    {
        String sql = "select id, cla_name claName, cla_start claStart, cla_end claEnd from class_info where cla_start = ?";
        return query(ClassEntity.class, sql, date);
    }

    /**
     * 按毕业时间查询班级数据
     * @param date 待查询的毕业时间
     * @return 查询到的班级列表
     * @throws Exception 异常
     */
    public List<ClassEntity> selectByEnd(LocalDate date) throws Exception
    {
        String sql = "select id, cla_name claName, cla_start claStart, cla_end claEnd from class_info where cla_end = ?";
        return query(ClassEntity.class, sql, date);
    }

    /**
     * 查询所有班级ID
     * @return 查询到的班级ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllID() throws Exception
    {
        String sql = "select id from class_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有班级名
     * @return 查询到的班级名集合
     * @throws Exception 异常
     */
    public Set<String> selectAllName() throws Exception
    {
        String sql = "select cla_name from class_info";
        return querySingleCol(sql);
    }

    /**
     * 查询班级表页数
     * @return 班级表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("class_info") / 10);
    }

    /**
     * 查询所有班级(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的班级列表
     * @throws Exception 异常
     */
    public List<ClassEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, cla_name claName, cla_start claStart, cla_end claEnd from class_info order by cla_end desc limit 10 offset ?";
        return query(ClassEntity.class, sql, offset);
    }

    /**
     * 查询所有入学时间
     * @return 查询到的入学时间集合
     * @throws Exception 异常
     */
    public Set<LocalDate> selectAllStart() throws Exception
    {
        String sql = "select distinct cla_start from class_info";
        return querySingleCol(sql);
    }

    /**
     * 查询所有毕业时间
     * @return 查询到的毕业时间集合
     * @throws Exception 异常
     */
    public Set<LocalDate> selectAllEnd() throws Exception
    {
        String sql = "select distinct cla_end from class_info";
        return querySingleCol(sql);
    }
}