package com.example.tour.util;

public class ValidationUtil {

    public static void validateRating(Double rating) {
        if(rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이여야 합니다.");
        }
    }
}
