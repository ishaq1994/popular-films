package com.ishaq.popularfilm;

import com.ishaq.popularfilm.utils.Utility;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 */
public class UtilityTest {

    @Test
    public void formatDateTest(){


        String formatDate= Utility.formatDate("2020-11-19");
        assertEquals("19-11-2020",formatDate);
    }
}