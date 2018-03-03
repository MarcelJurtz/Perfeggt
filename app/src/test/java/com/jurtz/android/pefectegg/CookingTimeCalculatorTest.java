package com.jurtz.android.pefectegg;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CookingTimeCalculatorTest {

    @Test
    public void calcBoilingPoint_A() throws Exception {
        double height = 300;

        double actual = CookingTimeCalculator.calcBoilingPoint(height);
        double expected = 98.947;

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void calcBoilingPoint_B() throws Exception {
        double height = 800;

        double actual = CookingTimeCalculator.calcBoilingPoint(height);
        double expected = 97.193;

        assertEquals(expected, actual, 0.01);
    }


    @Test
    public void calcCookingTimeMinutes_A() throws Exception {
        double weight = 60;
        double boilingpoint = 98.947;
        double temperature = SettingsHelper.TEMP_DEG_FRIDGE;
        int tInside = SettingsHelper.TEMP_DEG_SOFT;


        double actual = CookingTimeCalculator.calcCookingTimeMinutes(weight, boilingpoint, temperature, tInside);
        double expected = 4.76;

        assertEquals(expected, actual, 0.02); // delta ca. 1 second
    }

    @Test
    public void calcCookintTimeMinutes_B() throws Exception {
        double weight = 80;
        double height = 800;
        double boilingPoint = CookingTimeCalculator.calcBoilingPoint(height);
        double temperature = SettingsHelper.TEMP_DEG_ROOM;
        int tInside = SettingsHelper.TEMP_DEG_MEDIUM;

        double actual = CookingTimeCalculator.calcCookingTimeMinutes(weight, boilingPoint, temperature, tInside);
        double expected = 7.183;

        assertEquals(expected, actual, 0.02);
    }

}