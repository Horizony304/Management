package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity
{
    private Integer id;
    private String couName;
    private String couMajor;
    private Integer couCre;

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n课程名：" + couName +
                "\n所属专业：" + couMajor +
                "\n学分：" + couCre;
    }
}
