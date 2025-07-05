package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeachEntity
{
    private Integer id;
    private Integer teaId;
    private Integer couId;
    private LocalDate teachStart;
    private LocalDate teachEnd;

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n教师ID：" + teaId +
                "\n课程ID：" + couId +
                "\n开课时间：" + teachStart +
                "\n结课时间：" + teachEnd;
    }
}
