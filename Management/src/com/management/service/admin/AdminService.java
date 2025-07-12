package com.management.service.admin;

import com.management.dao.admin.AdminDAO;
import com.management.dao.clas.ClassDAO;
import com.management.dao.course.CourseDAO;
import com.management.dao.grade.GradeDAO;
import com.management.dao.student.StuDAO;
import com.management.dao.teach.TeachDAO;
import com.management.dao.teacher.TeaDAO;
import com.management.dao.teacou.TeaCouDAO;
import com.management.entities.*;
import com.management.utils.SystemUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AdminService
{
    private AdminEntity admin;  //当前操作的管理员
    StuDAO stuDAO = new StuDAO();           //操作学生表
    TeaDAO teaDAO = new TeaDAO();           //操作教师表
    ClassDAO claDAO = new ClassDAO();       //操作班级表
    CourseDAO couDAO = new CourseDAO();     //操作课程表
    GradeDAO gDAO = new GradeDAO();         //操作成绩表
    TeaCouDAO teaCouDAO = new TeaCouDAO();  //操作教师-课程表
    TeachDAO teachDAO = new TeachDAO();     //操作授课计划表

    private Set<Integer> ID;        //打开子功能界面时的所有已存在主键ID(主键)
    private Set<Integer> SID;        //打开子功能界面时的所有已存在学生ID
    private Set<String> STUID;      //打开子功能界面时的所有已存在学号
    private Set<Integer> CLAID;     //打开子功能界面时的所有已存在班级ID
    private Set<Integer> TEAID;     //打开子功能界面时的所有已存在教师ID
    private Set<Integer> COUID;     //打开子功能界面时的所有已存在课程ID
    private Set<Integer> TEACHID;     //打开子功能界面时的所有已存在课程ID
    private Set<String> NAME;       //打开子功能界面时的所有已存在姓名
    private Set<LocalDate> START;   //打开子功能界面时的所有已存在开始时间
    private Set<LocalDate> END;     //打开子功能界面时的所有已存在结束时间
    private Set<String> MAJOR;      //打开子功能界面时的所有已存在专业
    private Set<Integer> CRE;        //打开子功能界面时的所有已存在学分

    private static final Set<Integer> SELECT_TWO = Set.of(0, 1);    //共2个选项
    private static final Set<Integer> SELECT_THREE = Set.of(0, 1, 2); //共3个选项
    private static final Set<Integer> FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6, 7);       //功能主界面的选项
    private static final Set<Integer> STU_FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8);//学生功能界面的选项
    private static final Set<Integer> TEA_FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6, 7);   //教师功能界面的选项
    private static final Set<Integer> CLA_FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8);//班级功能界面的选项
    private static final Set<Integer> COU_FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8);//课程功能界面的选项
    private static final Set<Integer> G_FUNCTIONS = Set.of(0, 1, 2, 3, 4);        //成绩功能界面的选项
    private static final Set<Integer> TEA_COU_FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5);     //教师-课程功能界面的选项
    private static final Set<Integer> TEACH_FUNCTIONS = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8);     //授课计划功能界面的选项

    /**
     * 实现管理员的登录
     */
    public void login()
    {
        Scanner sc = new Scanner(System.in);
        int select; //是否重新输入
        int chance = 3; //最多尝试3次，3次失败后就锁机5秒
        String username = "";
        String pwd = "";
        do
        {
            System.out.print("请输入用户名：");
            username = sc.nextLine();
            System.out.print("请输入密码：");
            pwd = sc.nextLine();
            if (login(username, pwd))   //登录成功
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
            System.out.print("用户名或者密码错误，您还剩" + chance + "次机会(按1重新尝试，按0返回主界面)：");
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

        if (login(username, pwd))   //登录成功
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
     * @param username 输入的用户名
     * @param pwd 输入的密码
     * @return 可以登录为true，否则为false
     */
    private boolean login(String username, String pwd)
    {
        AdminDAO adminDAO = new AdminDAO();
        try
        {
            admin = adminDAO.selectByName(username);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        if (admin == null)  //没有该用户名
            return false;
        else if (!admin.getAdPwd().equals(pwd)) //密码不正确
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
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.学生");
        System.out.println("2.教师");
        System.out.println("3.班级");
        System.out.println("4.课程");
        System.out.println("5.选课与成绩");
        System.out.println("6.教师-课程");
        System.out.println("7.授课计划");
        System.out.println("0.退出登录");
        System.out.print("请根据功能前面的数字选择您要操作的对象：");
    }

    /**
     * 操作功能主界面
     */
    private void functions()
    {
        SystemUtil.clearScreen();
        showFunctions();
        int select = SystemUtil.inputSelect(FUNCTIONS);

        switch (select) //进入相应的子功能界面
        {
            case 0 -> SystemUtil.userEntry(1);
            case 1 -> stuFunctions();
            case 2 -> teaFunctions();
            case 3 -> claFunctions();
            case 4 -> couFunctions();
            case 5 -> gFunctions();
            case 6 -> teaCouFunctions();
            case 7 -> teachFunctions();
        }
    }

    /**
     * 学生功能界面
     */
    private void showStuFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.增加学生");
        System.out.println("2.删除学生");
        System.out.println("3.修改学生");
        System.out.println("4.查询所有学生");
        System.out.println("5.按学号查询学生");
        System.out.println("6.按姓名查询学生");
        System.out.println("7.按性别查询学生");
        System.out.println("8.按班级查询学生");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作学生功能界面
     */
    private void stuFunctions()
    {
        try //初始化学生表中要用到的的部分字段集合
        {
            ID = stuDAO.selectAllID();
            STUID = stuDAO.selectAllStu();
            CLAID = claDAO.selectAllID();
            NAME = stuDAO.selectAllName();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前学生相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showStuFunctions();

        int select = SystemUtil.inputSelect(STU_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> addStudent();
            case 2 -> delStudent();
            case 3 -> modStudent();
            case 4 -> selStudent();
            case 5 -> selStudentByStu();
            case 6 -> selStudentByName();
            case 7 -> selStudentByGen();
            case 8 -> selStudentByCla();
        }
    }

    /**
     * 增加学生
     */
    private void addStudent()
    {
        String stuId = SystemUtil.getValidString("请输入新学生的学号(输入0可退出操作)：", s -> s.matches("\\d{8}"), "学号格式错误，请输入8位数字学号！", STUID, false, "当前系统已存在该学号，输入无效！");
        if (stuId.isEmpty())
            stuFunctions();
        String stuName = SystemUtil.getValidString("请输入新学生的姓名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", null, false, null);
        if (stuName.isEmpty())
            stuFunctions();
        String stuGender = SystemUtil.getValidString("请输入新学生的性别(输入0可退出操作)：", s -> "男".equals(s) || "女".equals(s), "性别只有\"男\"和\"女\"两个选项！", null, false, null);
        if (stuGender.isEmpty())
            stuFunctions();
        LocalDate stuBirth = SystemUtil.getValidDate("请输入新学生的出生日期(输入0可退出操作)：", null , null);
        if (stuBirth == null)
            stuFunctions();
        String stuPwd = SystemUtil.getValidString("请输入新学生的密码(输入0可退出操作)：", s -> !s.isEmpty(), "密码不能为空！", null, false, null);
        if (stuPwd.isEmpty())
            stuFunctions();
        int claId = SystemUtil.getValidInteger("请输入新学生的班级ID(输入0可退出操作)：", CLAID, "当前系统不存在该班级ID，输入无效！");
        if (claId == 0)
            stuFunctions();
        StudentEntity student = new StudentEntity(null, stuId, stuName, stuGender, stuBirth, stuPwd, claId);
        System.out.println("新学生的信息如下：");
        System.out.println(student);
        System.out.println("------------------------------");
        System.out.print("确认增加？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            stuFunctions();
        else
        {
            try
            {
                int insertID = stuDAO.insert(student);
                if (insertID > 0)
                {
                    System.out.println("增加成功！该学生分配到的ID是" + insertID);
                    SystemUtil.anyReturn();
                    stuFunctions();
                }
            }
            catch (SQLException _)
            {
                System.out.println("增加失败，请重新操作！");
                stuFunctions();
            }
        }
    }

    /**
     * 删除学生，级联删除成绩
     */
    private void delStudent()
    {
        int id = SystemUtil.getValidInteger("请输入待删除学生的ID(输入0可退出操作)：", ID, "当前系统不存在该学生ID，输入无效！");
        if (id == 0)
            stuFunctions();
        try
        {
            StudentEntity student = stuDAO.selectByID(id);
            System.out.println("该学生的信息如下：");
            System.out.println(student);
            System.out.println("------------------------------");
            List<GradeEntity> grades = gDAO.selectByStu(id);
            if (!grades.isEmpty())
            {
                System.out.println("该学生在系统中存在成绩数据如下：");
                SystemUtil.printList(grades);
                System.out.println("删除学生将导致以上数据级联删除！");
            }
        }
        catch (Exception _)
        {
            // Actually, this exception will never be caught.
            // We have prerequisite argument validation before real execution.
        }
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            stuFunctions();
        else
        {
            try
            {
                int delete = stuDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    stuFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                stuFunctions();
            }
        }
    }

    /**
     * 修改学生，可保证原子性
     */
    private void modStudent()
    {
        int id = SystemUtil.getValidInteger("请输入待修改学生的ID(输入0可退出操作)：", ID, "当前系统不存在该学生ID，输入无效！");
        if (id == 0)
            stuFunctions();
        StudentEntity student = null;
        int select;
        try
        {
            student = stuDAO.selectByID(id);
        }
        catch (Exception _) {}
        //学号
        System.out.println("该学生的学号为：" + student.getStuId());
        System.out.print("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String stuId = "";
        switch (select)
        {
            case 0 -> stuFunctions();
            case 1 ->
            {
                stuId = SystemUtil.getValidString("请输入该学生的新学号(输入0可退出操作)：", s -> s.matches("\\d{8}"), "学号格式错误，请输入8位数字学号！", STUID, false, "当前系统已存在该学号，输入无效！");
                if (stuId.isEmpty())
                    stuFunctions();
            }
            case 2 -> stuId = student.getStuId();
        }
        //姓名
        System.out.println("该学生的姓名为：" + student.getStuName());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String stuName = "";
        switch (select)
        {
            case 0 -> stuFunctions();
            case 1 ->
            {
                stuName = SystemUtil.getValidString("请输入该学生的新姓名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", null, false, null);
                if (stuName.isEmpty())
                    stuFunctions();
            }
            case 2 -> stuName = student.getStuName();
        }
        //性别
        System.out.println("该学生的性别为：" + student.getStuGender());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String stuGender = "";
        switch (select)
        {
            case 0 -> stuFunctions();
            case 1 ->
            {
                stuGender = SystemUtil.getValidString("请输入该学生的新性别(输入0可退出操作)：", s -> "男".equals(s) || "女".equals(s), "性别只有\"男\"和\"女\"两个选项！", null, false, null);
                if (stuGender.isEmpty())
                    stuFunctions();
            }
            case 2 -> stuGender = student.getStuGender();
        }
        //出生日期
        System.out.println("该学生的出生日期为：" + student.getStuBirth());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        LocalDate stuBirth = null;
        switch (select)
        {
            case 0 -> stuFunctions();
            case 1 ->
            {
                stuBirth = SystemUtil.getValidDate("请输入该学生的新出生日期(输入0可退出操作)：", null, null);
                if (stuBirth == null)
                    stuFunctions();
            }
            case 2 -> stuBirth = student.getStuBirth();
        }
        //密码
        System.out.println("该学生的密码为：" + student.getStuPwd());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String stuPwd = "";
        switch (select)
        {
            case 0 -> stuFunctions();
            case 1 ->
            {
                stuPwd = SystemUtil.getValidString("请输入该学生的新密码(输入0可退出操作)：", s -> !s.isEmpty(), "密码不能为空！", null, false, null);
                if (stuPwd.isEmpty())
                    stuFunctions();
            }
            case 2 -> stuPwd = student.getStuPwd();
        }
        //班级ID
        System.out.println("该学生的班级ID为：" + student.getClaId());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        int claId = 0;
        switch (select)
        {
            case 0 -> stuFunctions();
            case 1 ->
            {
                claId = SystemUtil.getValidInteger("请输入该学生的新班级ID(输入0可退出操作)：", CLAID, "当前系统不存在该班级ID，输入无效！");
                if (claId == 0)
                    stuFunctions();
            }
            case 2 -> claId = student.getClaId();
        }

        StudentEntity studentNew = new StudentEntity(student.getId(), stuId, stuName, stuGender, stuBirth, stuPwd, claId);
        System.out.println("该学生的新信息如下：");
        System.out.println(studentNew);
        System.out.println("------------------------------");
        System.out.print("确认修改？按1确认，按0取消：");
        select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            stuFunctions();
        else
        {
            try
            {
                int modify = stuDAO.modify(studentNew, id);
                if (modify > 0)
                {
                    System.out.println("修改成功！");
                    SystemUtil.anyReturn();
                    stuFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("修改失败，请重新操作！");
                stuFunctions();
            }
        }
    }

    /**
     * 查询所有学生(支持分页)
     */
    private void selStudent()
    {
        List<StudentEntity> students;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = stuDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前学生表数据为空！");
                SystemUtil.anyReturn();
                stuFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                students = stuDAO.selectByPage(pageNum);
                SystemUtil.printList(students);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            stuFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前学生相关数据有异常，请稍后再试！");
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
     * 按学号查询学生
     */
    private void selStudentByStu()
    {
        String stuId = SystemUtil.getValidString("请输入您要查询的学号(输入0可退出操作)：", s -> s.matches("\\d{8}"), "学号格式错误，请输入8位数字学号！", STUID, true, "当前系统不存在该学号，输入无效！");
        if (stuId.isEmpty())
            stuFunctions();
        try
        {
            StudentEntity student = stuDAO.selectByStu(stuId);
            System.out.println("该学生的信息如下：");
            System.out.println(student);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            stuFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前学生相关数据有异常，请稍后再试！");
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
     * 按姓名查询学生(可能重名)
     */
    private void selStudentByName()
    {
        String stuName = SystemUtil.getValidString("请输入您要查询的姓名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", NAME, true, "当前系统不存在该姓名，输入无效！");
        if (stuName.isEmpty())
            stuFunctions();
        try
        {
            List<StudentEntity> students = stuDAO.selectByName(stuName);    //一定不为空
            SystemUtil.printList(students);
            SystemUtil.anyReturn();
            stuFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前学生相关数据有异常，请稍后再试！");
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
     * 按性别查询学生，可能为空
     */
    private void selStudentByGen()
    {
        String stuGender = SystemUtil.getValidString("请输入您要查询的性别(输入0可退出操作)：", s -> "男".equals(s) || "女".equals(s), "性别只有\"男\"和\"女\"两个选项！", null, false, null);
        if (stuGender.isEmpty())
            stuFunctions();
        try
        {
            List<StudentEntity> students = stuDAO.selectByGen(stuGender);
            if (students.isEmpty()) //可能为空
                System.out.println("当前" + stuGender + "学生数据为空！");
            else
                SystemUtil.printList(students);
            SystemUtil.anyReturn();
            stuFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前学生相关数据有异常，请稍后再试！");
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
     * 按班级查询学生
     */
    private void selStudentByCla()
    {
        int claId = SystemUtil.getValidInteger("请输入您要查询的班级ID(输入0可退出操作)：", CLAID, "当前系统不存在该班级ID，输入无效！");
        if (claId == 0)
            stuFunctions();
        try
        {
            List<StudentEntity> students = stuDAO.selectByCla(claId);
            if (students.isEmpty()) //可能为空
                System.out.println("当前班级ID" + claId + "中学生数据为空！");
            else
                SystemUtil.printList(students);
            SystemUtil.anyReturn();
            stuFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前学生相关数据有异常，请稍后再试！");
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
     * 教师功能界面
     */
    private void showTeaFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.增加教师");
        System.out.println("2.删除教师");
        System.out.println("3.修改教师");
        System.out.println("4.查询所有教师");
        System.out.println("5.按ID查询教师");
        System.out.println("6.按姓名查询教师");
        System.out.println("7.按性别查询教师");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作教师功能界面
     */
    private void teaFunctions()
    {
        try //初始化教师表中的部分字段集合
        {
            ID = teaDAO.selectAllID();
            NAME = teaDAO.selectAllName();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showTeaFunctions();

        int select = SystemUtil.inputSelect(TEA_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> addTeacher();
            case 2 -> delTeacher();
            case 3 -> modTeacher();
            case 4 -> selTeacher();
            case 5 -> selTeacherById();
            case 6 -> selTeacherByName();
            case 7 -> selTeacherByGen();
        }
    }

    /**
     * 增加教师
     */
    private void addTeacher()
    {
        String teaName = SystemUtil.getValidString("请输入新教师的姓名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", null, false, null);
        if (teaName.isEmpty())
            teaFunctions();
        String teaGender = SystemUtil.getValidString("请输入新教师的性别(输入0可退出操作)：", s -> "男".equals(s) || "女".equals(s), "性别只有\"男\"和\"女\"两个选项！", null, false, null);
        if (teaGender.isEmpty())
            teaFunctions();
        String teaPwd = SystemUtil.getValidString("请输入新教师的密码(输入0可退出操作)：", s -> !s.isEmpty(), "密码不能为空！", null, false, null);
        if (teaPwd.isEmpty())
            teaFunctions();
        TeacherEntity teacher = new TeacherEntity(null, teaName, teaGender, teaPwd);
        System.out.println("新教师的信息如下：");
        System.out.println(teacher);
        System.out.print("确认增加？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teaFunctions();
        else
        {
            try
            {
                int insertID = teaDAO.insert(teacher);
                if (insertID > 0)
                {
                    System.out.println("增加成功！该教师分配到的ID是" + insertID);
                    SystemUtil.anyReturn();
                    teaFunctions();
                }
            }
            catch (SQLException _)
            {
                System.out.println("增加失败，请重新操作！");
                teaFunctions();
            }
        }
    }

    /**
     * 删除教师，级联删除教师-课程，连锁删除授课计划，连锁删除成绩
     */
    private void delTeacher()
    {
        int id = SystemUtil.getValidInteger("请输入待删除教师的ID(输入0可退出操作)：", ID, "当前系统不存在该教师ID，输入无效！");
        if (id == 0)
            teaFunctions();
        try
        {
            TeacherEntity teacher = teaDAO.selectByID(id);
            System.out.println("该教师的信息如下：");
            System.out.println(teacher);
            System.out.println("------------------------------");
            List<TeaCouEntity> teaCous = teaCouDAO.selectByTea(id);
            List<TeachEntity> teaches = teachDAO.selectByTeaCou(teaCous);
            List<GradeEntity> grades = gDAO.selectByTeach(teaches);
            //教师课程存在，授课计划存在，成绩存在
            if (!teaCous.isEmpty() && !teaches.isEmpty() && !grades.isEmpty())
            {
                System.out.println("该教师在系统中存在教师-课程数据如下：");
                SystemUtil.printList(teaCous);
                System.out.println("上述教师-课程在系统中存在授课计划数据如下：");
                SystemUtil.printList(teaches);
                System.out.println("上述授课计划在系统中存在成绩数据如下：");
                SystemUtil.printList(grades);
                System.out.println("删除教师将导致以上数据被级联删除");
            }
            //教师课程存在，授课计划存在，成绩不存在
            else if (!teaCous.isEmpty() && !teaches.isEmpty())
            {
                System.out.println("该教师在系统中存在教师-课程数据如下：");
                SystemUtil.printList(teaCous);
                System.out.println("上述教师-课程在系统中存在授课计划数据如下：");
                SystemUtil.printList(teaches);
                System.out.println("删除教师将导致以上数据被级联删除");
            }
            //教师课程存在，授课计划不存在，成绩不存在
            else if (!teaCous.isEmpty())
            {
                System.out.println("该教师在系统中存在教师-课程数据如下：");
                SystemUtil.printList(teaCous);
                System.out.println("删除教师将导致以上数据被级联删除");
            }
        }
        catch (Exception _) {}
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teaFunctions();
        else
        {
            try
            {
                int delete = teaDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    teaFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                teaFunctions();
            }
        }
    }

    /**
     * 修改教师，可保证原子性
     */
    private void modTeacher()
    {
        int id = SystemUtil.getValidInteger("请输入待修改教师的ID(输入0可退出操作)：", ID, "当前系统不存在该教师ID，输入无效！");
        if (id == 0)
            teaFunctions();
        TeacherEntity teacher = null;
        int select;
        try
        {
            teacher = teaDAO.selectByID(id);
        }
        catch (Exception _) {}
        //姓名
        System.out.println("该教师的姓名为：" + teacher.getTeaName());
        System.out.print("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String teaName = "";
        switch (select)
        {
            case 0 -> teaFunctions();
            case 1 ->
            {
                teaName = SystemUtil.getValidString("请输入该教师的新姓名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", null, false, null);
                if (teaName.isEmpty())
                    teaFunctions();
            }
            case 2 -> teaName = teacher.getTeaName();
        }
        //性别
        System.out.println("该教师的性别为：" + teacher.getTeaGender());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String teaGender = "";
        switch (select)
        {
            case 0 -> teaFunctions();
            case 1 ->
            {
                teaGender = SystemUtil.getValidString("请输入该教师的新性别(输入0可退出操作)：", s -> "男".equals(s) || "女".equals(s), "性别只有\"男\"和\"女\"两个选项！", null, false, null);
                if (teaGender.isEmpty())
                    teaFunctions();
            }
            case 2 -> teaGender = teacher.getTeaGender();
        }
        //密码
        System.out.println("该教师的密码为：" + teacher.getTeaPwd());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String teaPwd = "";
        switch (select)
        {
            case 0 -> teaFunctions();
            case 1 ->
            {
                teaPwd = SystemUtil.getValidString("请输入该教师的新密码(输入0可退出操作)：", s -> !s.isEmpty(), "密码不能为空！", null, false, null);
                if (teaPwd.isEmpty())
                    teaFunctions();
            }
            case 2 -> teaPwd = teacher.getTeaPwd();
        }

        TeacherEntity teacherNew = new TeacherEntity(teacher.getId(), teaName, teaGender, teaPwd);
        System.out.println("该教师的新信息如下：");
        System.out.println(teacherNew);
        System.out.println("------------------------------");
        System.out.print("确认修改？按1确认，按0取消：");
        select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teaFunctions();
        else
        {
            try
            {
                int modify = teaDAO.modify(teacherNew, id);
                if (modify > 0)
                {
                    System.out.println("修改成功！");
                    SystemUtil.anyReturn();
                    teaFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("修改失败，请重新操作！");
                teaFunctions();
            }
        }
    }

    /**
     * 查询所有教师(支持分页)
     */
    private void selTeacher()
    {
        List<TeacherEntity> teachers;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = teaDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前教师表数据为空！");
                SystemUtil.anyReturn();
                teaFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                teachers = teaDAO.selectByPage(pageNum);
                SystemUtil.printList(teachers);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            teaFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师相关数据有异常，请稍后再试！");
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
     * 按ID查询教师
     */
    private void selTeacherById()
    {
        Integer teaId = SystemUtil.getValidInteger("请输入您要查询的教师ID(输入0可退出操作)：", ID, "当前系统不存在该教师ID，输入无效！");
        if (teaId == 0)
            teaFunctions();
        try
        {
            TeacherEntity teacher = teaDAO.selectByID(teaId);
            System.out.println("该教师的信息如下：");
            System.out.println(teacher);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            teaFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师相关数据有异常，请稍后再试！");
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
     * 按姓名查询教师(可能重名)
     */
    private void selTeacherByName()
    {
        String teaName = SystemUtil.getValidString("请输入您要查询的姓名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", NAME, true, "当前系统不存在该姓名，输入无效！");
        if (teaName.isEmpty())
            teaFunctions();
        try
        {
            List<TeacherEntity> teachers = teaDAO.selectByName(teaName);    //一定不为空
            SystemUtil.printList(teachers);
            SystemUtil.anyReturn();
            teaFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师相关数据有异常，请稍后再试！");
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
     * 按性别查询教师，可能为空
     */
    private void selTeacherByGen()
    {
        String teaGender = SystemUtil.getValidString("请输入您要查询的性别(输入0可退出操作)：", s -> "男".equals(s) || "女".equals(s), "性别只有\"男\"和\"女\"两个选项！", null, false, null);
        if (teaGender.isEmpty())
            teaFunctions();
        try
        {
            List<TeacherEntity> teachers = teaDAO.selectByGen(teaGender);
            if (teachers.isEmpty()) //可能为空
                System.out.println("当前" + teaGender + "教师数据为空！");
            else
                SystemUtil.printList(teachers);
            SystemUtil.anyReturn();
            teaFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师相关数据有异常，请稍后再试！");
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
     * 班级功能界面
     */
    private void showClaFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.增加班级");
        System.out.println("2.删除班级");
        System.out.println("3.修改班级");
        System.out.println("4.查询所有班级");
        System.out.println("5.按ID查询班级");
        System.out.println("6.按班级名查询班级");
        System.out.println("7.按入学时间查询班级");
        System.out.println("8.按毕业时间查询班级");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作班级功能界面
     */
    private void claFunctions()
    {
        try //初始化班级表中的部分字段集合
        {
            ID = claDAO.selectAllID();
            NAME = claDAO.selectAllName();
            START = claDAO.selectAllStart();
            END = claDAO.selectAllEnd();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前班级相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showClaFunctions();

        int select = SystemUtil.inputSelect(CLA_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> addClass();
            case 2 -> delClass();
            case 3 -> modClass();
            case 4 -> selClass();
            case 5 -> selClassById();
            case 6 -> selClassByName();
            case 7 -> selClassByStart();
            case 8 -> selClassByEnd();
        }
    }

    /**
     * 增加班级
     */
    private void addClass()
    {
        String claName = SystemUtil.getValidString("请输入新班级的班级名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "班级名不能有空白字符，不能为空！", NAME, false, "当前系统已存在该班级名，输入无效！");
        if (claName.isEmpty())
            claFunctions();
        LocalDate claStart = SystemUtil.getValidDate("请输入新班级的入学时间(输入0可退出操作)：", null, "入学时间只能是9月1日！", 9, 1);
        if (claStart == null)
            claFunctions();
        LocalDate claEnd = SystemUtil.getValidDate("请输入新班级的毕业时间(输入0可退出操作)：", claStart.getYear(), "毕业时间只能是6月30日！", 6, 30);
        if (claEnd == null)
            claFunctions();

        ClassEntity clas = new ClassEntity(null, claName, claStart, claEnd);
        System.out.println("新班级的信息如下：");
        System.out.println(clas);
        System.out.println("------------------------------");
        System.out.print("确认增加？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            claFunctions();
        else
        {
            try
            {
                int insertID = claDAO.insert(clas);
                if (insertID > 0)
                {
                    System.out.println("增加成功！该班级分配到的ID是" + insertID);
                    SystemUtil.anyReturn();
                    claFunctions();
                }
            }
            catch (SQLException _)
            {
                System.out.println("增加失败，请重新操作！");
                claFunctions();
            }
        }
    }

    /**
     * 删除班级，级联删除学生，连锁删除成绩
     */
    private void delClass()
    {
        int id = SystemUtil.getValidInteger("请输入待删除班级的ID(输入0可退出操作)：", ID, "当前系统不存在该班级ID，输入无效！");
        if (id == 0)
            claFunctions();
        try
        {
            ClassEntity clas = claDAO.selectByID(id);
            System.out.println("该班级的信息如下：");
            System.out.println(clas);
            System.out.println("------------------------------");
            List<StudentEntity> students = stuDAO.selectByCla(id);
            List<GradeEntity> grades = gDAO.selectByStu(students);
            //学生存在，成绩存在
            if (!students.isEmpty() && !grades.isEmpty())
            {
                System.out.println("该班级在系统中存在学生数据如下：");
                SystemUtil.printList(students);
                System.out.println("上述学生在系统中存在成绩数据如下：");
                SystemUtil.printList(grades);
                System.out.println("删除班级将导致以上数据级联删除！");
            }
            //学生存在，成绩不存在
            else if (!students.isEmpty())
            {
                System.out.println("该班级在系统中存在学生数据如下：");
                SystemUtil.printList(students);
                System.out.println("删除班级将导致以上数据级联删除！");
            }
        }
        catch (Exception _) {}
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            claFunctions();
        else
        {
            try
            {
                int delete = claDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    claFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                claFunctions();
            }
        }
    }

    /**
     * 修改班级，可保证原子性
     */
    private void modClass()
    {
        int id = SystemUtil.getValidInteger("请输入待修改班级的ID(输入0可退出操作)：", ID, "当前系统不存在该班级ID，输入无效！");
        if (id == 0)
            claFunctions();
        ClassEntity clas = null;
        int select;
        try
        {
            clas = claDAO.selectByID(id);
        }
        catch (Exception _) {}
        //班级名
        System.out.println("该班级的班级名为：" + clas.getClaName());
        System.out.print("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String claName = "";
        switch (select)
        {
            case 0 -> claFunctions();
            case 1 ->
            {
                claName = SystemUtil.getValidString("请输入该班级的新班级名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "姓名不能有空白字符，不能为空！", NAME, false, "当前系统已存在该班级名，输入无效！");
                if (claName.isEmpty())
                    claFunctions();
            }
            case 2 -> claName = clas.getClaName();
        }
        //入学时间
        System.out.println("该班级的入学时间为：" + clas.getClaStart());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        LocalDate claStart = null;
        switch (select)
        {
            case 0 -> claFunctions();
            case 1 ->
            {
                claStart = SystemUtil.getValidDate("请输入该班级的新入学时间(输入0可退出操作)：", null, "入学时间只能是9月1日！", 9, 1 );
                if (claStart == null)
                    claFunctions();
            }
            case 2 -> claStart = clas.getClaStart();
        }
        //毕业时间
        System.out.println("该班级的毕业时间为：" + clas.getClaEnd());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        LocalDate claEnd = null;
        switch (select)
        {
            case 0 -> claFunctions();
            case 1 ->
            {
                claEnd = SystemUtil.getValidDate("请输入该班级的新毕业时间(输入0可退出操作)：", claStart.getYear(), "毕业时间只能是6月30日！", 6, 30);
                if (claEnd == null)
                    claFunctions();
            }
            case 2 -> claEnd = clas.getClaEnd();
        }

        ClassEntity clasNew = new ClassEntity(clas.getId(), claName, claStart, claEnd);
        System.out.println("该班级的新信息如下：");
        System.out.println(clasNew);
        System.out.println("------------------------------");
        System.out.print("确认修改？按1确认，按0取消：");
        select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            claFunctions();
        else
        {
            try
            {
                int modify = claDAO.modify(clasNew, id);
                if (modify > 0)
                {
                    System.out.println("修改成功！");
                    SystemUtil.anyReturn();
                    claFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("修改失败，请重新操作！");
                claFunctions();
            }
        }
    }

    /**
     * 查询所有班级(支持分页)
     */
    private void selClass()
    {
        List<ClassEntity> classes;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = claDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前班级表数据为空！");
                SystemUtil.anyReturn();
                teaFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                classes = claDAO.selectByPage(pageNum);
                SystemUtil.printList(classes);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            claFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前班级相关数据有异常，请稍后再试！");
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
     * 按ID查询班级
     */
    private void selClassById()
    {
        Integer claId = SystemUtil.getValidInteger("请输入您要查询的班级ID(输入0可退出操作)：", ID, "当前系统不存在该班级ID，输入无效！");
        if (claId == 0)
            claFunctions();
        try
        {
            ClassEntity clas = claDAO.selectByID(claId);
            System.out.println("该班级的信息如下：");
            System.out.println(clas);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            claFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前班级相关数据有异常，请稍后再试！");
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
     * 按班级名查询班级
     */
    private void selClassByName()
    {
        String claName = SystemUtil.getValidString("请输入您要查询的班级名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "班级名不能有空白字符，不能为空！", NAME, true, "当前系统不存在该班级名，输入无效！");
        if (claName.isEmpty())
            claFunctions();
        try
        {
            ClassEntity clas = claDAO.selectByName(claName);    //一定不为空
            System.out.println("该班级的信息如下：");
            System.out.println(clas);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            claFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前班级相关数据有异常，请稍后再试！");
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
     * 按入学时间查询班级
     */
    private void selClassByStart()
    {
        LocalDate claStart = SystemUtil.getExistDate("请输入您要查询的入学时间(输入0可退出操作)：", START, "当前系统不存在该入学时间，输入无效！");
        if (claStart == null)
            claFunctions();
        try
        {
            List<ClassEntity> classes = claDAO.selectByStart(claStart);
            SystemUtil.printList(classes);
            SystemUtil.anyReturn();
            claFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前班级相关数据有异常，请稍后再试！");
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
     * 按毕业时间查询班级
     */
    private void selClassByEnd()
    {
        LocalDate claEnd = SystemUtil.getExistDate("请输入您要查询的毕业时间(输入0可退出操作)：", END, "当前系统不存在该毕业时间，输入无效！");
        if (claEnd == null)
            claFunctions();
        try
        {
            List<ClassEntity> classes = claDAO.selectByEnd(claEnd);
            SystemUtil.printList(classes);
            SystemUtil.anyReturn();
            claFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前班级相关数据有异常，请稍后再试！");
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
     * 课程功能界面
     */
    private void showCouFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.增加课程");
        System.out.println("2.删除课程");
        System.out.println("3.修改课程");
        System.out.println("4.查询所有课程");
        System.out.println("5.按ID查询课程");
        System.out.println("6.按课程名查询课程");
        System.out.println("7.按所属专业查询课程");
        System.out.println("8.按学分查询课程");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作课程功能界面
     */
    private void couFunctions()
    {
        try //初始化班级表中的部分字段集合
        {
            ID = couDAO.selectAllID();
            NAME = couDAO.selectAllName();
            MAJOR = couDAO.selectAllMajor();
            CRE = couDAO.selectAllCre();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前课程相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showCouFunctions();

        int select = SystemUtil.inputSelect(COU_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> addCourse();
            case 2 -> delCourse();
            case 3 -> modCourse();
            case 4 -> selCourse();
            case 5 -> selCourseById();
            case 6 -> selCourseByName();
            case 7 -> selCourseByMajor();
            case 8 -> selCourseByCre();
        }
    }

    /**
     * 增加课程
     */
    private void addCourse()
    {
        String couName = SystemUtil.getValidString("请输入新课程的课程名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "课程名不能有空白字符，不能为空！", NAME, false, "当前系统已存在该课程名，输入无效！");
        if (couName.isEmpty())
            couFunctions();
        String couMajor = SystemUtil.getValidString("请输入新课程的所属专业(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "所属专业不能有空白字符，不能为空！", null, false, null);
        if (couMajor == null)
            couFunctions();
        int couCre = SystemUtil.getIntInRange("请输入新课程的学分(输入0可退出操作)：", 0, 10, "学分必须是[1, 10]之间的整数！");
        if (couCre == 0)
            couFunctions();

        CourseEntity course = new CourseEntity(null, couName, couMajor, couCre);
        System.out.println("新课程的信息如下：");
        System.out.println(course);
        System.out.println("------------------------------");
        System.out.print("确认增加？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            couFunctions();
        else
        {
            try
            {
                int insertID = couDAO.insert(course);
                if (insertID > 0)
                {
                    System.out.println("增加成功！该课程分配到的ID是" + insertID);
                    SystemUtil.anyReturn();
                    couFunctions();
                }
            }
            catch (SQLException _)
            {
                System.out.println("增加失败，请重新操作！");
                couFunctions();
            }
        }
    }

    /**
     * 删除课程，级联删除教师-课程，连锁删除授课计划，连锁删除成绩
     */
    private void delCourse()
    {
        int id = SystemUtil.getValidInteger("请输入待删除课程的ID(输入0可退出操作)：", ID, "当前系统不存在该课程ID，输入无效！");
        if (id == 0)
            couFunctions();
        try
        {
            CourseEntity course = couDAO.selectByID(id);
            System.out.println("该课程的信息如下：");
            System.out.println(course);
            System.out.println("------------------------------");
            List<TeaCouEntity> teaCous = teaCouDAO.selectByCou(id);
            List<TeachEntity> teaches = teachDAO.selectByTeaCou(teaCous);
            List<GradeEntity> grades = gDAO.selectByTeach(teaches);
            //教师课程存在，授课计划存在，成绩存在
            if (!teaCous.isEmpty() && !teaches.isEmpty() && !grades.isEmpty())
            {
                System.out.println("该课程在系统中存在教师-课程数据如下：");
                SystemUtil.printList(teaCous);
                System.out.println("上述教师-课程在系统中存在授课计划数据如下：");
                SystemUtil.printList(teaches);
                System.out.println("上述授课计划在系统中存在成绩数据如下：");
                SystemUtil.printList(grades);
                System.out.println("删除课程将导致以上数据被级联删除");
            }
            //教师课程存在，授课计划存在，成绩不存在
            else if (!teaCous.isEmpty() && !teaches.isEmpty())
            {
                System.out.println("该课程在系统中存在教师-课程数据如下：");
                SystemUtil.printList(teaCous);
                System.out.println("上述教师-课程在系统中存在授课计划数据如下：");
                SystemUtil.printList(teaches);
                System.out.println("删除课程将导致以上数据被级联删除");
            }
            //教师课程存在，授课计划不存在，成绩不存在
            else if (!teaCous.isEmpty())
            {
                System.out.println("该课程在系统中存在教师-课程数据如下：");
                SystemUtil.printList(teaCous);
                System.out.println("删除课程将导致以上数据被级联删除");
            }
        }
        catch (Exception _) {}
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            couFunctions();
        else
        {
            try
            {
                int delete = couDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    couFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                couFunctions();
            }
        }
    }

    /**
     * 修改课程，可保证原子性
     */
    private void modCourse()
    {
        int id = SystemUtil.getValidInteger("请输入待修改课程的ID(输入0可退出操作)：", ID, "当前系统不存在该课程ID，输入无效！");
        if (id == 0)
            couFunctions();
        CourseEntity course = null;
        int select;
        try
        {
            course = couDAO.selectByID(id);
        }
        catch (Exception _) {}
        //课程名
        System.out.println("该课程的课程名为：" + course.getCouName());
        System.out.print("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String couName = "";
        switch (select)
        {
            case 0 -> couFunctions();
            case 1 ->
            {
                couName = SystemUtil.getValidString("请输入该课程的新课程名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "课程名不能有空白字符，不能为空！", NAME, false, "当前系统已存在该课程名，输入无效！");
                if (couName.isEmpty())
                    couFunctions();
            }
            case 2 -> couName = course.getCouName();
        }
        //所属专业
        System.out.println("该课程的所属专业为：" + course.getCouMajor());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        String couMajor = "";
        switch (select)
        {
            case 0 -> couFunctions();
            case 1 ->
            {
                couMajor = SystemUtil.getValidString("请输入该课程的新所属专业(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "所属专业不能有空白字符，不能为空！", null, false, null);
                if (couMajor.isEmpty())
                    couFunctions();
            }
            case 2 -> couMajor = course.getCouMajor();
        }
        //学分
        System.out.println("该课程的学分为：" + course.getCouCre());
        System.out.println("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        int couCre = 0;
        switch (select)
        {
            case 0 -> couFunctions();
            case 1 ->
            {
                couCre = SystemUtil.getIntInRange("请输入该课程的新学分(输入0可退出操作)：", 0, 10, "学分必须是[1, 10]之间的整数！");
                if (couCre == 0)
                    couFunctions();
            }
            case 2 -> couCre = course.getCouCre();
        }

        CourseEntity courseNew = new CourseEntity(course.getId(), couName, couMajor, couCre);
        System.out.println("该课程的新信息如下：");
        System.out.println(courseNew);
        System.out.println("------------------------------");
        System.out.print("确认修改？按1确认，按0取消：");
        select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            couFunctions();
        else
        {
            try
            {
                int modify = couDAO.modify(courseNew, id);
                if (modify > 0)
                {
                    System.out.println("修改成功！");
                    SystemUtil.anyReturn();
                    couFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("修改失败，请重新操作！");
                couFunctions();
            }
        }
    }

    /**
     * 查询所有课程(支持分页)
     */
    private void selCourse()
    {
        List<CourseEntity> courses;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = couDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前课程表数据为空！");
                SystemUtil.anyReturn();
                couFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                courses = couDAO.selectByPage(pageNum);
                SystemUtil.printList(courses);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            couFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前课程相关数据有异常，请稍后再试！");
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
     * 按ID查询课程
     */
    private void selCourseById()
    {
        Integer couId = SystemUtil.getValidInteger("请输入您要查询的课程ID(输入0可退出操作)：", ID, "当前系统不存在该课程ID，输入无效！");
        if (couId == 0)
            couFunctions();
        try
        {
            CourseEntity course = couDAO.selectByID(couId);
            System.out.println("该课程的信息如下：");
            System.out.println(course);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            couFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前课程相关数据有异常，请稍后再试！");
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
     * 按课程名查询课程
     */
    private void selCourseByName()
    {
        String couName = SystemUtil.getValidString("请输入您要查询的课程名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "课程名不能有空白字符，不能为空！", NAME, true, "当前系统不存在该课程名，输入无效！");
        if (couName.isEmpty())
            couFunctions();
        try
        {
            CourseEntity course = couDAO.selectByName(couName);    //一定不为空
            System.out.println("该课程的信息如下：");
            System.out.println(course);
            System.out.println("------------------------------");
            SystemUtil.anyReturn();
            couFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前课程相关数据有异常，请稍后再试！");
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
     * 按专业查询课程
     */
    private void selCourseByMajor()
    {
        String couMajor = SystemUtil.getValidString("请输入您要查询的专业名(输入0可退出操作)：", s -> !s.isEmpty() && !s.matches(".*\\s.*"), "专业名不能有空白字符，不能为空！", MAJOR, true, "当前系统不存在该专业对应的课程，输入无效！");
        if (couMajor.isEmpty())
            couFunctions();
        try
        {
            List<CourseEntity> courses = couDAO.selectByMajor(couMajor);    //一定不为空
            SystemUtil.printList(courses);
            SystemUtil.anyReturn();
            couFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前课程相关数据有异常，请稍后再试！");
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
     * 按学分查询课程
     */
    private void selCourseByCre()
    {
        Integer couCre = SystemUtil.getValidInteger("请输入您要查询的学分(输入0可退出操作)：", CRE, "当前系统不存在该学分对应的课程，输入无效！");
        if (couCre == 0)
            couFunctions();
        try
        {
            List<CourseEntity> courses = couDAO.selectByCre(couCre);    //一定不为空
            SystemUtil.printList(courses);
            SystemUtil.anyReturn();
            couFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前课程相关数据有异常，请稍后再试！");
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
     * 成绩功能界面(无增加功能，选课交给学生操作，赋分交给教师操作；无修改功能，修改分数交给教师操作)
     */
    private void showGFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.删除选课与成绩");
        System.out.println("2.查询所有选课与成绩");
        System.out.println("3.按授课计划ID查询成绩");
        System.out.println("4.按学生ID查询成绩");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作成绩功能界面
     */
    private void gFunctions()
    {
        try //初始化成绩表中要用到的的部分字段集合
        {
            ID = gDAO.selectAllID();
            TEACHID = teachDAO.selectAllID();
            SID = stuDAO.selectAllID();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前成绩相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showGFunctions();

        int select = SystemUtil.inputSelect(G_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> delGrade();
            case 2 -> selGrade();
            case 3 -> selGradeByTeach();
            case 4 -> selGradeByStu();
        }
    }

    /**
     * 删除选课与成绩
     */
    private void delGrade()
    {
        int id = SystemUtil.getValidInteger("请输入待删除选课与成绩的ID(输入0可退出操作)：", ID, "当前系统不存在该选课与成绩ID，输入无效！");
        if (id == 0)
            gFunctions();
        try
        {
            GradeEntity grade = gDAO.selectByID(id);
            System.out.println("该选课与成绩的信息如下：");
            System.out.println(grade);
            System.out.println("------------------------------");
        }
        catch (Exception _) {}
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            gFunctions();
        else
        {
            try
            {
                int delete = gDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    gFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                gFunctions();
            }
        }
    }

    /**
     * 查询所有选课与成绩(支持分页)
     */
    private void selGrade()
    {
        List<GradeEntity> grades;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = gDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前选课与成绩表数据为空！");
                SystemUtil.anyReturn();
                gFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                grades = gDAO.selectByPage(pageNum);
                SystemUtil.printList(grades);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            gFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前选课与成绩相关数据有异常，请稍后再试！");
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
     * 按授课计划ID查询成绩，顺带输出该授课计划对应的所有成绩的平均分、及格率、优秀率
     */
    private void selGradeByTeach()
    {
        int teachId = SystemUtil.getValidInteger("请输入您要查询的授课计划ID(输入0可退出操作)：", TEACHID, "当前系统不存在该授课计划ID，输入无效！");
        if (teachId == 0)
            teachFunctions();
        try
        {
            List<GradeEntity> grades = gDAO.selectByTeach(teachId);
            if (grades.isEmpty()) //可能为空
                System.out.println("当前授课计划ID" + teachId + "暂无学生选课！");
            else
            {
                SystemUtil.printList(grades);
                double avg = 0;     //平均分
                double pass = 0;    //及格率
                double exce = 0;    //优秀率
                int num = 0;        //数量
                for (GradeEntity grade : grades)
                {
                    if (grade.getGScore() == null)  //未赋分则视为0
                        num++;
                    else
                    {
                        double score = grade.getGScore();
                        avg += score;
                        num++;
                        if (score >= 90)    //优秀+及格
                        {
                            pass++;
                            exce++;
                        }
                        else if (score >= 60)   //及格
                            pass++;
                    }
                }
                //计算平均分、及格率、优秀率
                avg = avg / num;
                pass = pass / num;
                exce = exce / num;
                System.out.println("该授课计划对应的成绩数据经计算得：");
                System.out.println("平均分：" + String.format("%.2f", avg) + " 及格率：" + String.format("%.2f%%", pass * 100) + " 优秀率：" + String.format("%.2f%%", exce * 100));
            }
            SystemUtil.anyReturn();
            gFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前选课与成绩相关数据有异常，请稍后再试！");
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
     * 按学生ID查询成绩，顺带输出该学生对应的所有成绩的平均分、及格率、优秀率
     */
    private void selGradeByStu()
    {
        int stuId = SystemUtil.getValidInteger("请输入您要查询的学生ID(输入0可退出操作)：", SID, "当前系统不存在该学生ID，输入无效！");
        if (stuId == 0)
            gFunctions();
        try
        {
            List<GradeEntity> grades = gDAO.selectByStu(stuId);
            if (grades.isEmpty()) //可能为空
                System.out.println("当前学生ID" + stuId + "暂未选课！");
            else
            {
                SystemUtil.printList(grades);
                double avg = 0;     //平均分
                double pass = 0;    //及格率
                double exce = 0;    //优秀率
                int num = 0;        //数量
                for (GradeEntity grade : grades)
                {
                    if (grade.getGScore() == null)  //未赋分则视为0
                        num++;
                    else
                    {
                        double score = grade.getGScore();
                        avg += score;
                        num++;
                        if (score >= 90)    //优秀+及格
                        {
                            pass++;
                            exce++;
                        }
                        else if (score >= 60)   //及格
                            pass++;
                    }
                }
                //计算平均分、及格率、优秀率
                avg = avg / num;
                pass = pass / num;
                exce = exce / num;
                System.out.println("该学生对应的成绩数据经计算得：");
                System.out.println("平均分：" + String.format("%.2f", avg) + " 及格率：" + String.format("%.2f%%", pass * 100) + " 优秀率：" + String.format("%.2f%%", exce * 100));
            }
            SystemUtil.anyReturn();
            gFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前选课与成绩相关数据有异常，请稍后再试！");
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
     * 教师-课程功能界面(无修改功能)
     */
    private void showTeaCouFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.增加教师-课程");
        System.out.println("2.删除教师-课程");
        System.out.println("3.查询所有教师-课程");
        System.out.println("4.按教师ID查询教师-课程");
        System.out.println("5.按课程ID查询教师-课程");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作教师-课程功能界面
     */
    private void teaCouFunctions()
    {
        try //初始化教师-课程表中要用到的的部分字段集合
        {
            ID = teaCouDAO.selectAllID();
            TEAID = teaDAO.selectAllID();
            COUID = couDAO.selectAllID();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师-课程相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showTeaCouFunctions();

        int select = SystemUtil.inputSelect(TEA_COU_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> addTeaCou();
            case 2 -> delTeaCou();
            case 3 -> selTeaCou();
            case 4 -> selTeaCouByTea();
            case 5 -> selTeaCouByCou();
        }
    }

    /**
     * 增加教师-课程
     */
    private void addTeaCou()
    {
        int teaId = SystemUtil.getValidInteger("请输入新教师-课程的教师ID(输入0可退出操作)：", TEAID, "当前系统不存在该教师ID，输入无效！");
        if (teaId == 0)
            teaCouFunctions();
        int couId = SystemUtil.getValidInteger("请输入新教师-课程的课程ID(输入0可退出操作)：", COUID, "当前系统不存在该课程ID，输入无效！");
        if (couId == 0)
            teaCouFunctions();
        try
        {
            TeaCouEntity teaCouex = teaCouDAO.selectByTeaCou(teaId, couId);
            if (teaCouex != null)
            {
                System.out.println("此教师-课程在系统中已存在！");
                SystemUtil.anyReturn();
                teaCouFunctions();
            }
        }
        catch (Exception _) {}
        TeaCouEntity teaCou = new TeaCouEntity(null, teaId, couId);
        System.out.println("新教师-课程的信息如下：");
        System.out.println(teaCou);
        System.out.println("------------------------------");
        System.out.print("确认增加？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teaCouFunctions();
        else
        {
            try
            {
                int insertID = teaCouDAO.insert(teaCou);
                if (insertID > 0)
                {
                    System.out.println("增加成功！该教师-课程分配到的ID是" + insertID);
                    SystemUtil.anyReturn();
                    teaCouFunctions();
                }
            }
            catch (SQLException _)
            {
                System.out.println("增加失败，请重新操作！");
                teaCouFunctions();
            }
        }
    }

    /**
     * 删除教师-课程，级联删除授课计划，连锁删除成绩
     */
    private void delTeaCou()
    {
        int id = SystemUtil.getValidInteger("请输入待删除教师-课程的ID(输入0可退出操作)：", ID, "当前系统不存在该教师-课程ID，输入无效！");
        if (id == 0)
            teaCouFunctions();
        try
        {
            TeaCouEntity teaCou = teaCouDAO.selectByID(id);
            System.out.println("该教师-课程的信息如下：");
            System.out.println(teaCou);
            System.out.println("------------------------------");
            List<TeachEntity> teaches = teachDAO.selectByTeaCou(teaCou);
            List<GradeEntity> grades = gDAO.selectByTeach(teaches);
            //授课计划存在，成绩存在
            if (!teaches.isEmpty() && !grades.isEmpty())
            {
                System.out.println("该教师-课程在系统中存在授课计划数据如下：");
                SystemUtil.printList(teaches);
                System.out.println("上述授课计划在系统中存在成绩数据如下：");
                SystemUtil.printList(grades);
                System.out.println("删除教师-课程将导致以上数据级联删除！");
            }
            //授课计划存在，成绩不存在
            else if (!teaches.isEmpty())
            {
                System.out.println("该教师-课程在系统中存在授课计划数据如下：");
                SystemUtil.printList(teaches);
                System.out.println("删除教师-课程将导致以上数据级联删除！");
            }
        }
        catch (Exception _) {}
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teaCouFunctions();
        else
        {
            try
            {
                int delete = teaCouDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    teaCouFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                teaCouFunctions();
            }
        }
    }

    /**
     * 查询所有教师-课程(支持分页)
     */
    private void selTeaCou()
    {
        List<TeaCouEntity> teaCous;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = teaCouDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前教师-课程表数据为空！");
                SystemUtil.anyReturn();
                teaCouFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                teaCous = teaCouDAO.selectByPage(pageNum);
                SystemUtil.printList(teaCous);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            teaCouFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师-课程相关数据有异常，请稍后再试！");
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
     * 按教师ID查询教师-课程
     */
    private void selTeaCouByTea()
    {
        int teaId = SystemUtil.getValidInteger("请输入您要查询的教师ID(输入0可退出操作)：", TEAID, "当前系统不存在该教师ID，输入无效！");
        if (teaId == 0)
            teaCouFunctions();
        try
        {
            List<TeaCouEntity> teaCous = teaCouDAO.selectByTea(teaId);
            if (teaCous.isEmpty()) //可能为空
                System.out.println("当前教师ID" + teaId + "中教师-课程数据为空！");
            else
                SystemUtil.printList(teaCous);
            SystemUtil.anyReturn();
            teaCouFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师-课程相关数据有异常，请稍后再试！");
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
     * 按课程ID查询教师-课程
     */
    private void selTeaCouByCou()
    {
        int couId = SystemUtil.getValidInteger("请输入您要查询的课程ID(输入0可退出操作)：", COUID, "当前系统不存在该课程ID，输入无效！");
        if (couId == 0)
            teaCouFunctions();
        try
        {
            List<TeaCouEntity> teaCous = teaCouDAO.selectByTea(couId);
            if (teaCous.isEmpty()) //可能为空
                System.out.println("当前课程ID" + couId + "中教师-课程数据为空！");
            else
                SystemUtil.printList(teaCous);
            SystemUtil.anyReturn();
            teaCouFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前教师-课程相关数据有异常，请稍后再试！");
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
     * 授课计划功能界面
     */
    private void showTeachFunctions()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("管理员" + admin.getAdUname() + "您好：");
        System.out.println("1.增加授课计划");
        System.out.println("2.删除授课计划");
        System.out.println("3.修改授课计划");
        System.out.println("4.查询所有授课计划");
        System.out.println("5.按教师ID查询授课计划");
        System.out.println("6.按课程ID查询授课计划");
        System.out.println("7.按开课时间查询授课计划");
        System.out.println("8.按结课时间查询授课计划");
        System.out.println("0.返回上一级");
        System.out.print("请根据功能前面的数字选择您要完成的操作：");
    }

    /**
     * 操作授课计划功能界面
     */
    private void teachFunctions()
    {
        try //初始化授课计划表中要用到的的部分字段集合
        {
            ID = teachDAO.selectAllID();
            TEAID = teaDAO.selectAllID();
            COUID = couDAO.selectAllID();
            START = teachDAO.selectAllStart();
            END = teachDAO.selectAllEnd();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前授课计划相关数据有异常，请稍后再试！");
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

        SystemUtil.clearScreen();
        showTeachFunctions();

        int select = SystemUtil.inputSelect(TEACH_FUNCTIONS);
        switch (select)
        {
            case 0 -> functions();
            case 1 -> addTeach();
            case 2 -> delTeach();
            case 3 -> modTeach();
            case 4 -> selTeach();
            case 5 -> selTeachByTea();
            case 6 -> selTeachByCou();
            case 7 -> selTeachByStart();
            case 8 -> selTeachByEnd();
        }
    }

    /**
     * 增加授课计划，组合检查教师ID、课程ID
     */
    private void addTeach()
    {
        int teaId = SystemUtil.getValidInteger("请输入新授课计划的教师ID(输入0可退出操作)：", TEAID, "当前系统不存在该教师ID，输入无效！");
        if (teaId == 0)
            teachFunctions();
        int couId;
        while (true)    //确保正确依赖于教师-课程
        {
            couId = SystemUtil.getValidInteger("请输入新授课计划的课程ID(输入0可退出操作)：", COUID, "当前系统不存在该课程ID，输入无效！");
            if (couId == 0)
                teachFunctions();
            try
            {
                Set<Integer> couIds = teaCouDAO.selectCouByTea(teaId);
                if (couIds.isEmpty() || !couIds.contains(couId))  //拒绝增加
                {
                    System.out.println("该教师没有此课程的授课权，请先赋予权利！");
                    SystemUtil.anyReturn();
                    teachFunctions();
                }
                else    //可以增加
                    break;
            }
            catch (Exception _) {}
        }
        LocalDate teachStart = SystemUtil.getValidDate("请输入新授课计划的开课时间(输入0可退出操作)：", null, "开课时间只能是9月1日或2月15日！", 9, 1, 2, 15);
        if (teachStart == null)
            teachFunctions();
        try
        {
            TeachEntity teach = teachDAO.selectByAll(teaId, couId, teachStart);
            if (teach != null)
            {
                System.out.println("当前系统已有相同授课计划！");
                SystemUtil.anyReturn();
                teachFunctions();
            }
        }
        catch (Exception _) {}
        //结课时间自动安排 开课时间9月1日，则结课时间为第二年1月15日；开课时间2月15日，则结课时间为当年6月30日
        int endYear = teachStart.getMonthValue() == 9 ? teachStart.getYear() + 1 : teachStart.getYear();
        int endMonth = teachStart.getMonthValue() == 9 ? 1 : 6;
        int endDay = endMonth == 1 ? 15 : 30;
        LocalDate teachEnd = LocalDate.of(endYear, endMonth, endDay);

        TeachEntity teach = new TeachEntity(null, teaId, couId, teachStart, teachEnd);
        System.out.println("新授课计划的信息如下：");
        System.out.println(teach);
        System.out.println("------------------------------");
        System.out.print("确认增加？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teachFunctions();
        else
        {
            try
            {
                int insertID = teachDAO.insert(teach);
                if (insertID > 0)
                {
                    System.out.println("增加成功！该授课计划分配到的ID是" + insertID);
                    SystemUtil.anyReturn();
                    teachFunctions();
                }
            }
            catch (SQLException _)
            {
                System.out.println("增加失败，请重新操作！");
                teachFunctions();
            }
        }
    }

    /**
     * 删除授课计划，级联删除成绩
     */
    private void delTeach()
    {
        int id = SystemUtil.getValidInteger("请输入待删除授课计划的ID(输入0可退出操作)：", ID, "当前系统不存在该授课计划ID，输入无效！");
        if (id == 0)
            teachFunctions();
        try
        {
            TeachEntity teach = teachDAO.selectByID(id);
            System.out.println("该授课计划的信息如下：");
            System.out.println(teach);
            System.out.println("------------------------------");
            List<GradeEntity> grades = gDAO.selectByTeach(id);
            if (!grades.isEmpty())
            {
                System.out.println("该授课计划在系统中存在成绩数据如下：");
                SystemUtil.printList(grades);
                System.out.println("删除授课计划将导致以上数据级联删除！");
            }
        }
        catch (Exception _) {}
        System.out.print("确认删除？按1确认，按0取消：");
        int select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teachFunctions();
        else
        {
            try
            {
                int delete = teachDAO.delete(id);
                if (delete > 0)
                {
                    System.out.println("删除成功！");
                    SystemUtil.anyReturn();
                    teachFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("删除失败，请重新操作！");
                teachFunctions();
            }
        }
    }

    /**
     * 修改授课计划，仅允许修改开课时间，并且若该授课计划已经有学生选课，则不允许修改
     */
    private void modTeach()
    {
        int id = SystemUtil.getValidInteger("请输入待修改授课计划的ID(输入0可退出操作)：", ID, "当前系统不存在该授课计划ID，输入无效！");
        if (id == 0)
            teachFunctions();
        try
        {
            List<GradeEntity> grades = gDAO.selectByTeach(id);
            if (!grades.isEmpty())
            {
                System.out.println("该授课计划在系统中已经有学生选课，不允许修改！");
                SystemUtil.anyReturn();
                teachFunctions();
            }
        }
        catch (Exception _) {}
        TeachEntity teach = null;
        int select;
        try
        {
            teach = teachDAO.selectByID(id);
        }
        catch (Exception _) {}
        //开课时间
        System.out.println("该授课计划的开课时间为：" + teach.getTeachStart());
        System.out.print("是否修改？按1确认，按2跳过，按0退出：");
        select = SystemUtil.inputSelect(SELECT_THREE);
        LocalDate teachStart = null;
        switch (select)
        {
            case 0 -> teachFunctions();
            case 1 ->
            {
                teachStart = SystemUtil.getValidDate("请输入该授课计划的新开课时间(输入0可退出操作)：", null, "开课时间只能是9月1日或2月15日！", 9, 1, 2, 15);
                if (teachStart == null)
                    teachFunctions();
            }
            case 2 -> teachStart = teach.getTeachStart();
        }
        try
        {
            TeachEntity teach1 = teachDAO.selectByAll(teach.getTeaId(), teach.getCouId(), teachStart);
            if (teach1 != null)
            {
                System.out.println("当前系统已有相同授课计划！");
                SystemUtil.anyReturn();
                teachFunctions();
            }
        }
        catch (Exception _) {}
        //结课时间自动安排 开课时间9月1日，则结课时间为第二年1月15日；开课时间2月15日，则结课时间为当年6月30日
        int endYear = teachStart.getMonthValue() == 9 ? teachStart.getYear() + 1 : teachStart.getYear();
        int endMonth = teachStart.getMonthValue() == 9 ? 1 : 6;
        int endDay = endMonth == 1 ? 15 : 30;
        LocalDate teachEnd = LocalDate.of(endYear, endMonth, endDay);

        TeachEntity teachNew = new TeachEntity(teach.getId(), teach.getTeaId(), teach.getCouId(), teachStart, teachEnd);
        System.out.println("该授课计划的新信息如下：");
        System.out.println(teachNew);
        System.out.println("------------------------------");
        System.out.print("确认修改？按1确认，按0取消：");
        select = SystemUtil.inputSelect(SELECT_TWO);
        if (select == 0)
            teachFunctions();
        else
        {
            try
            {
                int modify = teachDAO.modify(teachNew, id);
                if (modify > 0)
                {
                    System.out.println("修改成功！");
                    SystemUtil.anyReturn();
                    teachFunctions();
                }
            }
            catch (SQLException e)
            {
                System.out.println("修改失败，请重新操作！");
                teachFunctions();
            }
        }
    }

    /**
     * 查询所有授课计划(支持分页)
     */
    private void selTeach()
    {
        List<TeachEntity> teaches;
        int select = 1;
        int pageNum = 1;    //当前页
        try
        {
            int totalNum = teachDAO.pageNum(); //总页数
            if (totalNum == 0)
            {
                System.out.println("当前授课计划表数据为空！");
                SystemUtil.anyReturn();
                teachFunctions();
            }
            while (select != 0)
            {
                System.out.println("======= 第 " + pageNum + " 页，共 " + totalNum + " 页 =======");
                teaches = teachDAO.selectByPage(pageNum);
                SystemUtil.printList(teaches);
                if (totalNum == 1)  //总共就一页
                {
                    SystemUtil.anyReturn(); //按回车键返回
                    select = 0;
                }
                else    //有多页
                {
                    if (pageNum == 1)   //第一页
                    {
                        System.out.println("1.下一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum += 1;
                    }
                    else if (pageNum == totalNum)   //最后一页
                    {
                        System.out.println("1.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_TWO);
                        pageNum -= 1;
                    }
                    else    //中间页
                    {
                        System.out.println("1.下一页");
                        System.out.println("2.上一页");
                        System.out.println("0.退出");
                        System.out.print("请选择：");
                        select = SystemUtil.inputSelect(SELECT_THREE);
                        if (select == 1)
                            pageNum += 1;
                        else
                            pageNum -= 1;
                    }
                }
            }
            teachFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前授课计划相关数据有异常，请稍后再试！");
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
     * 按教师ID查询授课计划(按时间降序输出)
     */
    private void selTeachByTea()
    {
        int teaId = SystemUtil.getValidInteger("请输入您要查询的教师ID(输入0可退出操作)：", TEAID, "当前系统不存在该教师ID，输入无效！");
        if (teaId == 0)
            teachFunctions();
        try
        {
            List<TeachEntity> teaches = teachDAO.selectByTea(teaId);
            if (teaches.isEmpty()) //可能为空
                System.out.println("当前教师ID" + teaId + "中授课计划数据为空！");
            else
                SystemUtil.printList(teaches);
            SystemUtil.anyReturn();
            teachFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前授课计划相关数据有异常，请稍后再试！");
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
     * 按课程ID查询授课计划(按时间降序输出)
     */
    private void selTeachByCou()
    {
        int couId = SystemUtil.getValidInteger("请输入您要查询的课程ID(输入0可退出操作)：", COUID, "当前系统不存在该课程ID，输入无效！");
        if (couId == 0)
            teachFunctions();
        try
        {
            List<TeachEntity> teaches = teachDAO.selectByTea(couId);
            if (teaches.isEmpty()) //可能为空
                System.out.println("当前课程ID" + couId + "中授课计划数据为空！");
            else
                SystemUtil.printList(teaches);
            SystemUtil.anyReturn();
            teachFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前授课计划相关数据有异常，请稍后再试！");
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
     * 按开课时间查询授课计划
     */
    private void selTeachByStart()
    {
        LocalDate teachStart = SystemUtil.getExistDate("请输入您要查询的开课时间(输入0可退出操作)：", START, "当前系统不存在该开课时间，输入无效！");
        if (teachStart == null)
            teachFunctions();
        try
        {
            List<TeachEntity> teaches = teachDAO.selectByStart(teachStart);
            SystemUtil.printList(teaches);
            SystemUtil.anyReturn();
            teachFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前授课计划相关数据有异常，请稍后再试！");
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
     * 按结课时间查询授课计划
     */
    private void selTeachByEnd()
    {
        LocalDate teachEnd = SystemUtil.getExistDate("请输入您要查询的结课时间(输入0可退出操作)：", END, "当前系统不存在该结课时间，输入无效！");
        if (teachEnd == null)
            teachFunctions();
        try
        {
            List<TeachEntity> teaches = teachDAO.selectByEnd(teachEnd);
            SystemUtil.printList(teaches);
            SystemUtil.anyReturn();
            teachFunctions();
        }
        catch (Exception _)
        {
            System.out.println("Sorry，当前授课计划相关数据有异常，请稍后再试！");
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
}