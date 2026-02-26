package com.example.tour.util;

public class RatingUtil {

    public static double round(Double value) {
        if (value == null) return 0.0;
        //    4.845 => 48.45 => 48 => 4.8
        return Math.round(value * 10) / 10.0;
    }
}
