package com.management.service.student;

import com.management.dao.course.CourseDAO;
import com.management.dao.grade.GradeDAO;
import com.management.dao.student.StuDAO;
import com.management.dao.teach.TeachDAO;
import com.management.dao.teacher.TeaDAO;
import com.management.entities.*;
import com.management.utils.SystemUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class StuService
{
    private StudentEntity student;  //当前操作的学生

    private static final Set<Integer> FUNCTIONS = Set.of(0, 1, 2, 3, 4);//功能主界面的选项


    /**
     * 实现学生的登录
     */
    public void login()
    {
        Scanner sc = new Scanner(System.in);
        int select; //是否重新输入
        int chance = 3; //最多尝试3次，3次失败后就锁机5秒
        String stuId = "";
        String pwd = "";
        do
        {
            System.out.print("请输入学号：");
            stuId = sc.nextLine();
            System.out.print("请输入密码：");
            pwd = sc.nextLine();
            if (login(stuId, pwd))   //登录成功
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
            System.out.print("学号或者密码错误，您还剩" + chance + "次机会(按1重新尝试，按0返回主界面)：");
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

        if (login(stuId, pwd))   //登录成功
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
     * @param stuId 输入的学生学号
     * @param pwd 输入的密码
     * @return 可以登录为true，否则为false
     */
    private boolean login(String stuId, String pwd)
    {
        StuDAO stuDAO = new StuDAO();
        try
        {
            student = stuDAO.selectByStu(stuId);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        if (student == null)    //没有该学号对应的学生
            return false;
        else if (!student.getStuPwd().equals(pwd))  //密码不正确
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
        System.out.println("学生" + student.getStuName() + "你好：");
        System.out.println("1.查看个人信息");
        System.out.println("2.增加选课");
        System.out.println("3.删除选课");
        System.out.println("4.查看成绩");
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
            case 0 -> SystemUtil.userEntry(3);
            case 1 -> selInformation();
            case 2 -> addGrade();
            case 3 -> delGrade();
            case 4 -> selGrade();
        }
    }

    /**
     * 查看自己的个人信息
     */
    private void selInformation()
    {
        System.out.println("个人信息如下：");
        System.out.println(student);
        SystemUtil.anyReturn();
        functions();
    }

    /**
     * 增加选课(前提：该授课计划还未开始)
     */
    private void addGrade()
    {
        CourseDAO courseDAO = new CourseDAO();
        TeachDAO teachDAO = new TeachDAO();
        TeaDAO teaDAO = new TeaDAO();
        GradeDAO gradeDAO = new GradeDAO();
        Set<Integer> TEACHID = new HashSet<>();
        try
        {
            List<CourseEntity> courses = courseDAO.selectAll();
            if (courses.isEmpty())
            {
                System.out.println("抱歉，当前系统没有课程信息，请等待一段时间！");
                SystemUtil.anyReturn();
                functions();
            }
            System.out.println("当前系统已有的课程如下：");
            SystemUtil.printList(courses);
            Set<String> NAME = courseDAO.selectAllName();
            String couName = SystemUtil.getValidString("请输入你要选择的课程名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "课程名不能有空白字符，不能为空！", NAME, true, "当前系统不存在该课程名，输入无效！");
            List<TeachEntity> teaches = teachDAO.selectByCouName(couName);
            if (teaches.isEmpty())
            {
                System.out.println("抱歉，该课程暂无可选的授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            System.out.println("该课程可选的授课计划如下：");
            for (TeachEntity teach : teaches)
            {
                TeacherEntity teacher = teaDAO.selectByID(teach.getTeaId());
                System.out.println("ID：" + teach.getId());
                System.out.println("教师姓名：" + teacher.getTeaName());
                System.out.println("开课时间：" + teach.getTeachStart());
                System.out.println("结课时间：" + teach.getTeachEnd());
                System.out.println("------------------------------");
                TEACHID.add(teach.getId());
            }
            int teachId = SystemUtil.getValidInteger("请输入你要选择的授课计划ID(输入0可退出操作)：", TEACHID, "输入无效！");
            if (teachId == 0)
                functions();
            GradeEntity grade = gradeDAO.selectByTeachStu(teachId, student.getId());
            if (grade != null)
            {
                System.out.println("你已选过该授课计划！");
                SystemUtil.anyReturn();
                functions();
            }
            GradeEntity gradeNew = new GradeEntity(null, teachId, student.getId(), null);
            int insert = gradeDAO.insert(gradeNew);
            if (insert > 0)
            {
                System.out.println("选课成功！");
                SystemUtil.anyReturn();
                functions();
            }
            else
            {
                System.out.println("选课失败，请重新操作！");
                SystemUtil.anyReturn();
                functions();
            }
        }
        catch (Exception _) {}
    }

    /**
     * 删除选课(前提：该授课计划还未开始)
     */
    private void delGrade()
    {
        TeachDAO teachDAO = new TeachDAO();
        GradeDAO gradeDAO = new GradeDAO();
        TeaDAO teaDAO = new TeaDAO();
        CourseDAO courseDAO = new CourseDAO();
        Set<Integer> TEACHID = new HashSet<>();
        try
        {
            List<TeachEntity> teaches = teachDAO.selectByStu(student.getId());
            if (teaches.isEmpty())
            {
                System.out.println("抱歉，你没有可删除的选课！");
                SystemUtil.anyReturn();
                functions();
            }
            System.out.println("你可删除的选课如下：");
            for (TeachEntity teach : teaches)
            {
                TeacherEntity teacher = teaDAO.selectByID(teach.getTeaId());
                CourseEntity course = courseDAO.selectByID(teach.getCouId());
                System.out.println("授课计划ID：" + teach.getId());
                System.out.println("教师姓名：" + teacher.getTeaName());
                System.out.println("课程名：" + course.getCouName());
                System.out.println("开课时间：" + teach.getTeachStart());
                System.out.println("结课时间：" + teach.getTeachEnd());
                System.out.println("------------------------------");
                TEACHID.add(teach.getId());
            }
            int teachId = SystemUtil.getValidInteger("请输入你要删除的授课计划ID(输入0可退出操作)：", TEACHID, "输入无效！");
            if (teachId == 0)
                functions();
            GradeEntity grade = gradeDAO.selectByTeachStu(teachId, student.getId());
            int delete = gradeDAO.delete(grade.getId());
            if (delete > 0)
            {
                System.out.println("删除成功！");
                SystemUtil.anyReturn();
                functions();
            }
            else
            {
                System.out.println("删除失败，请重新操作！");
                SystemUtil.anyReturn();
                functions();
            }
        }
        catch (Exception _) {}
    }

    /**
     * 查看成绩(按结课时间降序输出)
     */
    private void selGrade()
    {
        GradeDAO gradeDAO = new GradeDAO();
        TeachDAO teachDAO = new TeachDAO();
        TeaDAO teaDAO = new TeaDAO();
        CourseDAO courseDAO = new CourseDAO();
        try
        {
            List<GradeEntity> grades = gradeDAO.selectByStu(student.getId());
            if (grades.isEmpty())
            {
                System.out.println("抱歉，你没有选课记录！");
                SystemUtil.anyReturn();
                functions();
            }
            List<TeachEntity> teaches = teachDAO.selectByStu(student.getId());
            System.out.println("你的成绩列表如下：");
            for (GradeEntity grade : grades)
            {
                TeachEntity teach = teachDAO.selectByID(grade.getTeachId());
                TeacherEntity teacher = teaDAO.selectByID(teach.getTeaId());
                CourseEntity course = courseDAO.selectByID(teach.getCouId());
                System.out.println("教师名：" + teacher.getTeaName());
                System.out.println("课程名：" + course.getCouName());
                System.out.println("开课时间：" + teach.getTeachStart());
                System.out.println("结课时间：" + teach.getTeachEnd());
                System.out.println("分数：" + grade.getGScore());
                System.out.println("------------------------------");
            }
            SystemUtil.anyReturn();
            functions();
        }
        catch (Exception _) {}
    }
}