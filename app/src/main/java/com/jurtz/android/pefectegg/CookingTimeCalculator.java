package com.jurtz.android.pefectegg;

public class CookingTimeCalculator {
    public static double calcBoilingPoint(double heightAboveSeaLevelMeters) {
        double temp = 100;
        double desc = heightAboveSeaLevelMeters / 285;
        temp -= desc;
        return temp;
    }

    public static double calcCookingTimeMinutes(double weight, double boilingPoint, double temperature, int tInside) {
        return 0.465 * Math.pow(weight, 2.0 / 3.0) * Math.log(0.76 * ((boilingPoint - temperature) / (boilingPoint - tInside)));
    }
}
