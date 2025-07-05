package com.management.dao.teacou;

import com.management.dao.BaseDAO;
import com.management.entities.TeaCouEntity;
import com.management.entities.TeacherEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * 实现TeaCou教师-课程的CRUD
 * 增删改
 * 按ID查询，按教师ID查询，按课程ID查询
 */
public class TeaCouDAO extends BaseDAO
{
    /**
     * 增加一条师课数据
     * @param teaCou 待增加的师课
     * @return 主键值ID
     * @throws SQLException SQL异常
     */
    public int insert(TeaCouEntity teaCou) throws SQLException
    {
        String sql = "insert into tea_cou_info (tea_id, cou_id) values (?, ?)";
        return insertWithKey(sql, teaCou.getTeaId(), teaCou.getCouId());
    }

    /**
     * 删除一条师课数据
     * @param id 待删除的师课ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int delete(int id) throws SQLException
    {
        String sql = "delete from tea_cou_info where id = ?";
        return update(sql, id);
    }

    /**
     * 修改一条师课数据
     * @param teaCou 修改后的师课数据
     * @param id 待修改的师课ID
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public int modify(TeaCouEntity teaCou, int id) throws SQLException
    {
        String sql = "update tea_cou_info set tea_id = ?, cou_id = ? where id = ?";
        return update(sql, teaCou.getTeaId(), teaCou.getCouId(), id);
    }

    /**
     * 查询所有师课
     * @return 所有师课的列表
     * @throws Exception 异常
     */
    public List<TeaCouEntity> selectAll() throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId from tea_cou_info";
        return query(TeaCouEntity.class, sql);
    }

    /**
     * 按ID查询一条师课数据
     * @param id 待查询的师课ID
     * @return 查询到的师课
     * @throws Exception 异常
     */
    public TeaCouEntity selectByID(int id) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId from tea_cou_info where id = ?";
        return querySingle(TeaCouEntity.class, sql, id);
    }

    /**
     * 按教师ID查询师课数据
     * @param teaId 待查询的教师ID
     * @return 查询到的师课列表
     * @throws Exception 异常
     */
    public List<TeaCouEntity> selectByTea(int teaId) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId from tea_cou_info where tea_id = ?";
        return query(TeaCouEntity.class, sql, teaId);
    }

    /**
     * 按教师ID查询被授权的课程ID
     * @param teaId 待查询的教师ID
     * @return 该教师ID被授权的课程ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectCouByTea(int teaId) throws Exception
    {
        String sql = "select cou_id from tea_cou_info where tea_id = ?";
        return querySingleCol(sql, teaId);
    }

    /**
     * 按课程ID查询师课数据
     * @param couId 待查询的课程ID
     * @return 查询到的师课列表
     * @throws Exception 异常
     */
    public List<TeaCouEntity> selectByCou(int couId) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId from tea_cou_info where cou_id = ?";
        return query(TeaCouEntity.class, sql, couId);
    }

    /**
     * 查询所有ID
     * @return 查询到的ID集合
     * @throws Exception 异常
     */
    public Set<Integer> selectAllID() throws Exception
    {
        String sql = "select id from tea_cou_info";
        return querySingleCol(sql);
    }

    /**
     * 查询教师-课程表页数
     * @return 教师-课程表页数
     * @throws SQLException SQL异常
     */
    public int pageNum() throws SQLException
    {
        return (int) Math.ceil((double) totalCount("tea_cou_info") / 10);
    }

    /**
     * 查询所有教师-课程(分页版)
     * @param pageNum 页码(从1开始)
     * @return 当前页码的教师列表
     * @throws Exception 异常
     */
    public List<TeaCouEntity> selectByPage(int pageNum) throws Exception
    {
        int offset = (pageNum - 1) * 10;
        String sql = "select id, tea_id teaId, cou_id couId from tea_cou_info limit 10 offset ?";
        return query(TeaCouEntity.class, sql, offset);
    }

    public TeaCouEntity selectByTeaCou(int teaId, int couId) throws Exception
    {
        String sql = "select id, tea_id teaId, cou_id couId from tea_cou_info where tea_id = ? and cou_id = ?";
        return querySingle(TeaCouEntity.class, sql, teaId, couId);
    }
}