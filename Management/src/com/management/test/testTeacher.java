package com.management.test;

import com.management.service.teacher.TeaService;
import org.junit.Test;

public class testTeacher
{
    @Test
    public void testAddScore()
    {
        TeaService teaService = new TeaService();
        teaService.addScore();
    }
}
