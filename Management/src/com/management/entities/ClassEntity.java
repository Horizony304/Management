package com.management.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassEntity
{
    private Integer id;
    private String claName;
    private LocalDate claStart;
    private LocalDate claEnd;

    @Override
    public String toString()
    {
        String outputID = id == null ? "(未分配)" : String.valueOf(id);    //增加时显示未分配
        return "ID：" + outputID +
                "\n班级名：" + claName +
                "\n入学时间：" + claStart +
                "\n毕业时间：" + claEnd;
    }
}