package com.management.test;

import com.management.entities.GradeEntity;
import org.junit.Test;

public class testGrade
{
    @Test
    public void testNull()
    {
        GradeEntity grade = new GradeEntity();
        System.out.println(grade.getGScore());
    }
}
