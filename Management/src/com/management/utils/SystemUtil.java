package com.management.utils;

import com.management.entities.*;
import com.management.service.admin.AdminService;
import com.management.service.student.StuService;
import com.management.service.teacher.TeaService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

public class SystemUtil
{
    private static final Scanner sc = new Scanner(System.in);

    /**
     * 对系统主界面进行操作
     */
    public static void systemEntry()
    {
        int select;
        clearScreen();
        mainInterface();
        do
        {
            System.out.print("请输入(0-3)：");
            String input = sc.nextLine();
            try
            {
                select = Integer.parseInt(input);
                switch (select)
                {
                    case 0 ->   //退出系统
                    {
                        System.out.println("退出成功！欢迎下次使用！");
                        System.exit(0);
                    }
                    case 1 -> userEntry(select);    //管理员界面
                    case 2 -> userEntry(select);    //教师界面
                    case 3 -> userEntry(select);    //学生界面
                    default -> System.out.println("无效选项，请重新输入！");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("无效选项，请重新输入！");
                select = -1;
            }
        } while (select != 1 && select != 2 && select != 3);
    }

    /**
     * 对用户界面进行操作
     *
     * @param role 用户角色 1->管理 2->教师 3->学生
     */
    public static void userEntry(int role)
    {
        clearScreen();
        int select;
        userInterface(role);
        do
        {
            System.out.print("请输入(0-1)：");
            String input = sc.nextLine();
            try
            {
                select = Integer.parseInt(input);
                switch (select)
                {
                    case 0 ->
                    {
                        clearScreen();
                        systemEntry();
                    }
                    case 1 ->
                    {
                        if (role == 1) new AdminService().login();
                        if (role == 2) new TeaService().login();
                        if (role == 3) new StuService().login();
                    }
                    default -> System.out.println("无效选项，请重新输入！");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("无效选项，请重新输入！");
                select = -1;
            }
        } while (select != 1);
    }

    /**
     * 系统主界面
     */
    private static void mainInterface()
    {
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("************************************");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("*             1. Admin             *");
        System.out.println("*                                  *");
        System.out.println("*            2. Teacher            *");
        System.out.println("*                                  *");
        System.out.println("*            3. Student            *");
        System.out.println("*                                  *");
        System.out.println("*             0. Leave             *");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("************************************");
    }

    /**
     * 用户界面
     *
     * @param role 用户角色 1->管理 2->教师 3->学生
     */
    private static void userInterface(int role)
    {
        String user = "";
        if (role == 1)
            user = "管理界面";
        if (role == 2)
            user = "教师界面";
        if (role == 3)
            user = "学生界面";
        System.out.println("====================================");
        System.out.println("      科利大学学生信息管理系统      ");
        System.out.println("            " + user + "            ");
        System.out.println("************************************");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("*             1. Login             *");
        System.out.println("*                                  *");
        System.out.println("*             0. Leave             *");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("*                                  *");
        System.out.println("************************************");
    }

    /**
     * 实现清屏效果
     */
    public static void clearScreen()
    {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
        try
        {
            pb.inheritIO().start().waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 保证输入的选项有效
     *
     * @param set 有效的数字集合
     * @return 有效的选项
     */
    public static int inputSelect(Set<Integer> set)
    {
        int select;
        while (true)
        {
            try
            {
                String input = sc.nextLine();
                select = Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                select = -1;
            }
            if (set.contains(select))
                break;
            else
                System.out.print("无效选项，请重新输入：");
        }
        return select;
    }

    /**
     * 确保输入的字符串字段有效，若输入0则返回空串
     *
     * @param prompt    提示的输入内容
     * @param predicate 判断字符串格式是否有效的函数式接口
     * @param errorMes  字符串格式无效时反馈的错误信息
     * @param set       已存在的字段集合
     * @param exist     若为true则表示字段已存在才属于有效，若为false则表示字段不存在才属于有效
     * @param existMes  不满足存不存在的条件时反馈的错误信息
     * @return 完全有效的字符串或0
     */
    public static String getValidString(String prompt, Predicate<String> predicate, String errorMes, Set<String> set, boolean exist, String existMes)
    {
        String str = "";
        boolean right = false;
        do
        {
            System.out.print(prompt);
            str = sc.nextLine();
            try
            {
                if (Integer.parseInt(str) == 0)
                    return "";
            }
            catch (NumberFormatException _)
            {
            }
            if (!predicate.test(str))
                System.out.println(errorMes);
            else if (set != null)
            {
                if (exist)
                    right = set.contains(str);
                else
                    right = !set.contains(str);
                if (!right)
                    System.out.println(existMes);
            }
        } while (!predicate.test(str) || set != null && !right);
        return str;
    }

    /**
     * 确保输入的数字字段有效且包含在set里面，或者返回退出操作的0
     * @param prompt 提示输入的内容
     * @param set    判断是否包含在依赖的表里面(用于外键或删除，不存在则报错)
     * @param errMes 输入无效时反馈的错误信息
     * @return 有效的数字字段或0
     */
    public static Integer getValidInteger(String prompt, Set<Integer> set, String errMes)
    {
        int id;
        do
        {
            System.out.print(prompt);
            String input = sc.nextLine();
            try
            {
                id = Integer.parseInt(input);
                if (set != null && set.contains(id) || id == 0)
                    break;
                else
                    System.out.println(errMes);
            }
            catch (NumberFormatException _)
            {
                System.out.println("请输入有效的整数！");
            }
        } while (true);
        return id;
    }

    /**
     * 确保输入的数字在[min+1, max]之间且为整数，学分为[1, 10]，分数为[1, 100]，分数可以为空(暂未赋分，此时视为0分)
     * @param prompt 提示的输入内容
     * @param min 最小值，设置为0是因为0也可以走return res这个分支，但是有效的结果不能为0，0只是代表退出操作
     * @param max 最大值
     * @param errMes 不在范围内时反馈的错误信息
     * @return 有效的整数且在范围内
     */
    public static Integer getRangeInteger(String prompt, int min, int max, String errMes)
    {
        int res;
        while (true)
        {
            System.out.print(prompt);
            String input = sc.nextLine();
            try
            {
                res = Integer.parseInt(input);
                //有效的整数，接下来判断是否在范围内
                if (res >= min && res <= max)   //在范围内
                    return res;
                else                            //不在范围内
                    System.out.println(errMes);
            }
            catch (NumberFormatException _)
            {
                System.out.println("请输入有效的整数！");
            }
        }
    }

    /**
     * 确保输入的日期字段有效
     * @param prompt   提示的输入内容
     * @param big 上一个年份，保证输入的日期年份大于big
     * @param errorMes 不符合valid时反馈的错误信息
     * @param valid    有效的数字(按日期的月日顺序输入，比如[9, 1], [1, 15], [2, 15], [6, 30])
     * @return 有效的日期字段
     */
    public static LocalDate getValidDate(String prompt, Integer big, String errorMes, int... valid)
    {
        String str = "";
        String s = "";
        LocalDate date;
        while (true)
        {
            System.out.print(prompt);
            str = sc.nextLine();
            s = str;
            try
            {
                if (Integer.parseInt(s) == 0)
                    return null;
            }
            catch (NumberFormatException _) {}  //下划线可以作为未使用的异常变量名
            try
            {
                date = LocalDate.parse(str);
                //日期格式正确后往下判断两个可选条件
                if (big != null)    //优先判断年份是否满足
                {
                    int yearValue = date.getYear();
                    if (yearValue <= big)
                    {
                        System.out.println("年份需要大于" + big + "！");
                        continue;   //跳过循环剩下的部分
                    }
                }
                if (valid != null && valid.length > 0)  //日期格式正确，判断是否满足指定的月日
                {
                    int monthValue = date.getMonthValue();
                    int dayValue = date.getDayOfMonth();
                    for (int i = 0; i < valid.length; i += 2)
                    {
                        if (monthValue == valid[i] && dayValue == valid[i + 1])
                            return date;
                    }
                    //不满足指定的月日
                    System.out.println(errorMes);
                }
                else    //没有多余的判断，主要用在增加出生日期上
                    return date;
            }
            catch (DateTimeParseException _)
            {
                System.out.println("日期错误，请以yyyy-MM-dd格式输入合理的日期！");
            }
        }
    }

    /**
     * 确保输入的日期字段有效且包含在set里面，或者返回退出操作的0
     * @param prompt 提示输入的内容
     * @param set    判断是否包含在依赖的表里面不存在则报错，
     * @param errMes 输入无效时反馈的错误信息
     * @return 有效的数字字段或0
     */
    public static LocalDate getExistDate(String prompt, Set<LocalDate> set, String errMes)
    {
        LocalDate result = null;
        while (true)
        {
            result = getValidDate(prompt, null, null);  //接收到格式正确的日期字段或null
            if (result == null) //退出操作
                return null;
            //格式正确的日期字段，接下来检查是否在已存在的集合里
            if (set.size() == 0)    //不存在
                System.out.println(errMes);
            else if (!set.contains(result)) //不存在
                System.out.println(errMes);
            else    //存在
                return result;
        }
    }

    /**
     * 按回车键返回
     */
    public static void anyReturn()
    {
        System.out.print("按回车键返回...");
        try
        {
            //读取一个字符，阻塞直到用户输入
            System.in.read();
            //清除输入缓冲区的剩余字符，包括换行符
            while (System.in.available() > 0)
                System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 格式化输出实体列表    保证了不为空再调用此方法
     *
     * @param list 待输出的实体列表
     * @param <T>  实体类型
     */
    public static <T> void printList(List<T> list)
    {
        String role = "";
        T t = list.get(0);
        if (t instanceof StudentEntity)
            role = "学生";
        else if (t instanceof TeacherEntity)
            role = "教师";
        else if (t instanceof ClassEntity)
            role = "班级";
        else if (t instanceof CourseEntity)
            role = "课程";
        else if (t instanceof GradeEntity)
            role = "成绩";
        else if (t instanceof TeaCouEntity)
            role = "教师-课程";
        else
            role = "授课计划";

        int len = list.size();
        for (int i = 1; i <= len; i++)
        {
            System.out.println(role + "-" + i + " 👇");
            System.out.println(list.get(i - 1));
            System.out.println("------------------------------");
        }
    }
}