package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity
{
    private Integer id;
    private String stuId;
    private String stuName;
    private String stuGender;
    private LocalDate stuBirth;
    private String stuPwd;
    private Integer claId;

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n学号：" + stuId +
                "\n姓名：" + stuName +
                "\n性别：" + stuGender +
                "\n出生日期：" + stuBirth +
                "\n密码：" + stuPwd +
                "\n班级ID：" + claId;
    }
}
