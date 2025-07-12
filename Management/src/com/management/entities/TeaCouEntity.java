package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// TeacherCourseEntity
// TeacherCourseRelationEntity
public class TeaCouEntity
{
    private Integer id;
    private Integer teaId;
    private Integer couId; // courseId

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n教师ID：" + teaId +
                "\n课程ID：" + couId;
    }
}
