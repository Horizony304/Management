package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeEntity
{
    private Integer id;
    private Integer teachId;
    private Integer stuId;
    private Integer gScore;

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n授课计划ID：" + teachId +
                "\n学生ID：" + stuId +
                "\n分数：" + gScore;
    }
}
