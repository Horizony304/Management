package com.management.service.teacher;

import com.management.dao.course.CourseDAO;
import com.management.dao.grade.GradeDAO;
import com.management.dao.student.StuDAO;
import com.management.dao.teach.TeachDAO;
import com.management.dao.teacher.TeaDAO;
import com.management.dao.teacou.TeaCouDAO;
import com.management.entities.*;
import com.management.utils.SystemUtil;

import java.util.*;

public class TeaService
{
    private TeacherEntity teacher;  //当前操作的教师

    private static final Set<Integer> SELECT_TWO = Set.of(0, 1);    //共2个选项
    private static final Set<Integer> SELECT_THREE = Set.of(0, 1, 2); //共3个选项
    private static final Set<Integer> FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6);//功能主界面的选项


    /**
     * 实现教师的登录
     */
    public void login()
    {
        Scanner sc = new Scanner(System.in);
        int select; //是否重新输入
        int chance = 3; //最多尝试3次，3次失败后就锁机5秒
        int teaId = 0;
        String pwd = "";
        do
        {
            System.out.print("请输入教师ID：");
            try
            {
                teaId = Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("请输入有效的整数！");
                continue;
            }
            System.out.print("请输入密码：");
            pwd = sc.nextLine();
            if (login(teaId, pwd))   //登录成功
                break;
            chance--;
            if (chance == 0)    //机会用完，锁机5秒并返回主界面
            {
                System.out.println("登录尝试次数过多，系统锁定中......");
                try
                {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                SystemUtil.systemEntry();
            }
            System.out.print("教师ID或者密码错误，您还剩" + chance + "次机会(按1重新尝试，按0返回主界面)：");
            do
            {
                String input = sc.nextLine();
                try
                {
                    select = Integer.parseInt(input);
                }
                catch (NumberFormatException e)
                {
                    System.out.print("无效选项！按1重新尝试，按0返回主界面：");
                    continue;
                }
                if (select == 1)
                    break;
                else if (select == 0)
                    SystemUtil.systemEntry();
                else
                    System.out.print("无效选项！按1重新尝试，按0返回主界面：");
            } while (true);
        } while (true);

        if (login(teaId, pwd))   //登录成功
        {
            System.out.println("登录中......");
            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            functions();
        }
    }

    /**
     * 判断是否可以登录
     *
     * @param teaId 输入的教师ID
     * @param pwd   输入的密码
     * @return 可以登录为true，否则为false
     */
    private boolean login(int teaId, String pwd)
    {
        TeaDAO teaDAO = new TeaDAO();
        try
        {
            teacher = teaDAO.selectByID(teaId);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        if (teacher == null)    //没有该教师ID对应的教师
            return false;
        else if (!teacher.getTeaPwd().equals(pwd))  //密码不正确
            return false;
        else
            return true;
    }

    /**
     * 功能主界面
     */
    private void showFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("教师" + teacher.getTeaName() + "您好：");
        System.out.println("1.查看选课学生信息");
        System.out.println("2.查看选课学生成绩");
        System.out.println("3.查看可教课程");
        System.out.println("4.查看授课计划");
        System.out.println("5.录入分数");
        System.out.println("6.修改分数");
        System.out.println("0.退出登录");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作功能主界面
     */
    private void functions()
    {
        SystemUtil.clearScreen();
        showFunctions();
        int select = SystemUtil.inputSelect(FUNCTIONS);

        switch (select) //进入相应的功能界面
        {
            case 0 -> SystemUtil.userEntry(2);
            case 1 -> selStuByStuId();
            case 2 -> selGByStuId();
            case 3 -> selTeaCou();
            case 4 -> selTeach();
            case 5 -> addScore();
            case 6 -> modScore();
        }
    }

    /**
     * 按学生学号查看学生信息(前提：该学生属于自己的授课计划里的学生列表)
     */
    private void selStuByStuId()
    {
        List<StudentEntity> students = new ArrayList<>();
        StuDAO stuDAO = new StuDAO();
        Set<String> SId = new HashSet<>();
        try
        {
            students = stuDAO.selectByTea(teacher.getId());
            if (students.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可查询的学生！");
                SystemUtil.anyReturn();
                functions();
            }
            for (StudentEntity student : students)
            {
                String sid = student.getStuId();
                SId.add(sid);
            }
            String stuId = SystemUtil.getValidString("请输入您要查询的学号(输入0可退出操作)：", s -> s.matches("\\d{8}"), "学号格式错误，请输入8位数字学号！", SId, true, "该学生未选择您的授课计划，输入无效！");
            StudentEntity student = stuDAO.selectByStu(stuId);
            System.out.println("该学生的信息如下：");
            System.out.println(student);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            functions();
        }
        catch (Exception _)
        {
        }
    }

    /**
     * 按学生学号查看学生成绩(前提：该学生属于自己的授课计划里的学生列表；按时间降序输出)
     */
    private void selGByStuId()
    {
        List<StudentEntity> students = new ArrayList<>();
        StuDAO stuDAO = new StuDAO();
        GradeDAO gDAO = new GradeDAO();
        Set<String> SId = new HashSet<>();
        try
        {
            students = stuDAO.selectByTea(teacher.getId());
            if (students.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可查询的学生！");
                SystemUtil.anyReturn();
                functions();
            }
            for (StudentEntity student : students)
            {
                String sid = student.getStuId();
                SId.add(sid);
            }
            String stuId = SystemUtil.getValidString("请输入您要查询的学号(输入0可退出操作)：", s -> s.matches("\\d{8}"), "学号格式错误，请输入8位数字学号！", SId, true, "该学生未选择您的授课计划，输入无效！");
            List<GradeEntity> grades = gDAO.selectByStu(teacher.getId(), stuId);
            System.out.println("该学生的成绩如下：");
            SystemUtil.printList(grades);
            SystemUtil.anyReturn();
            functions();
        }
        catch (Exception _)
        {
        }
    }

    /**
     * 查看可教课程
     */
    private void selTeaCou()
    {
        TeaCouDAO teaCouDAO = new TeaCouDAO();
        CourseDAO courseDAO = new CourseDAO();
        try
        {
            List<TeaCouEntity> teaCous = teaCouDAO.selectByTea(teacher.getId());
            if (teaCous.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可教的课程！");
                SystemUtil.anyReturn();
                functions();
            }
            System.out.println("您可教的课程信息如下：");
            List<CourseEntity> courses = courseDAO.selectByTea(teacher.getId());
            SystemUtil.printList(courses);
            SystemUtil.anyReturn();
            functions();
        }
        catch (Exception _) {}
    }

    /**
     * 查看授课计划(按时间降序输出)
     */
    private void selTeach()
    {
        TeachDAO teachDAO = new TeachDAO();
        try
        {
            List<TeachEntity> teaches = teachDAO.selectByTea(teacher.getId());
            if (teaches.isEmpty())
            {
                System.out.println("抱歉，您暂时没有授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            System.out.println("您可查询到的授课计划如下：");
            SystemUtil.printList(teaches);
            SystemUtil.anyReturn();
            functions();
        }
        catch (Exception _) {}
    }

    /**
     * 录入分数(前提：该成绩对应的授课计划属于自己并且已结课)
     */
    public void addScore()
    {
        List<StudentEntity> students = new ArrayList<>();
        StuDAO stuDAO = new StuDAO();
        GradeDAO gDAO = new GradeDAO();
        TeachDAO teachDAO = new TeachDAO();
        CourseDAO couDAO = new CourseDAO();
        try
        {
            students = stuDAO.selectByTea(teacher.getId());
            if (students.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可录分的授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            List<TeachEntity> teaches = teachDAO.selectByTeaEnd(teacher.getId());
            List<GradeEntity> grades = gDAO.selectByTeachNull(teaches);
            if (grades.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可录分的授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            for (GradeEntity grade : grades)
            {
                StudentEntity student = stuDAO.selectByID(grade.getStuId());
                CourseEntity course = couDAO.selectByID(teachDAO.selectByID(grade.getTeachId()).getCouId());
                System.out.println("学号：" + student.getStuId());
                System.out.println("姓名：" + student.getStuName());
                System.out.println("课程名：" + course.getCouName());
                int score = SystemUtil.getRangeInteger("请输入您要录入的分数(输入0可退出操作)：", 0, 100, "分数必须是[1, 100]之间的整数！");
                if (score == 0)
                    functions();
                GradeEntity gradeNew = new GradeEntity(null, grade.getTeachId(), grade.getStuId(), score);
                int modify = gDAO.modify(gradeNew, grade.getId());
                System.out.println("录入成功！");
                SystemUtil.anyReturn();
            }
            functions();
        }
        catch (Exception _) {}
    }

    /**
     * 修改分数(前提：该成绩对应的授课计划属于自己并且已结课)
     */
    private void modScore()
    {
        List<StudentEntity> students = new ArrayList<>();
        StuDAO stuDAO = new StuDAO();
        GradeDAO gDAO = new GradeDAO();
        TeachDAO teachDAO = new TeachDAO();
        CourseDAO couDAO = new CourseDAO();
        try
        {
            students = stuDAO.selectByTea(teacher.getId());
            if (students.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可修改分数的授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            List<TeachEntity> teaches = teachDAO.selectByTeaEnd(teacher.getId());
            List<GradeEntity> grades = gDAO.selectByTeachNonNull(teaches);
            if (grades.isEmpty())
            {
                System.out.println("抱歉，您暂时没有可修改分数的授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            for (GradeEntity grade : grades)
            {
                StudentEntity student = stuDAO.selectByID(grade.getStuId());
                TeachEntity teach = teachDAO.selectByID(grade.getTeachId());
                CourseEntity course = couDAO.selectByID(teach.getCouId());
                System.out.println("学号：" + student.getStuId());
                System.out.println("姓名：" + student.getStuName());
                System.out.println("课程名：" + course.getCouName());
                System.out.println("开课时间：" + teach.getTeachStart());
                System.out.println("结课时间：" + teach.getTeachEnd());
                System.out.println("分数：" + grade.getGScore());
                System.out.print("是否修改？按1确认，按0跳过：");
                int select = SystemUtil.inputSelect(SELECT_TWO);
                if (select == 0)
                    continue;
                int score = SystemUtil.getRangeInteger("请输入您要修改的分数(输入0可退出操作)：", 0, 100, "分数必须是[1, 100]之间的整数！");
                if (score == 0)
                    functions();
                GradeEntity gradeNew = new GradeEntity(null, grade.getTeachId(), grade.getStuId(), score);
                int modify = gDAO.modify(gradeNew, grade.getId());
                System.out.println("修改成功！");
                SystemUtil.anyReturn();
            }
            functions();
        }
        catch (Exception _) {}
    }
}