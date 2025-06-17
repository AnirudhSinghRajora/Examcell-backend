package com.examcell.resultgen.util;

public class GradeCalculatorUtil {
    public static double calculateGradePoint(double marksObtained) {
        if (marksObtained >= 90) return 10.0;
        if (marksObtained >= 80) return 9.0;
        if (marksObtained >= 70) return 8.0;
        if (marksObtained >= 60) return 7.0;
        if (marksObtained >= 50) return 6.0;
        return 0.0;
    }
} 