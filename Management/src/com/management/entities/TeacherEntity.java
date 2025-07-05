package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEntity
{
    private Integer id;
    private String teaName;
    private String teaGender;
    private String teaPwd;

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n姓名：" + teaName +
                "\n性别：" + teaGender +
                "\n密码：" + teaPwd;
    }
}
