package com.examcell.resultgen.util;

import org.springframework.stereotype.Component;

@Component
public class GradeCalculatorUtil {
    public static double calculateGradePoint(double marksObtained) {
        if (marksObtained >= 90) return 10.0;
        if (marksObtained >= 80) return 9.0;
        if (marksObtained >= 70) return 8.0;
        if (marksObtained >= 60) return 7.0;
        if (marksObtained >= 50) return 6.0;
        if (marksObtained >= 40) return 5.0;
        if (marksObtained >= 35) return 4.0;
        return 0.0;
    }
    
    public static String calculateGrade(double marksObtained) {
        if (marksObtained >= 90) return "A+";
        if (marksObtained >= 80) return "A";
        if (marksObtained >= 70) return "B+";
        if (marksObtained >= 60) return "B";
        if (marksObtained >= 50) return "C+";
        if (marksObtained >= 40) return "C";
        if (marksObtained >= 35) return "D";
        return "F";
    }
    
    public static double roundToNearest005(double value) {
        return Math.round(value * 20.0) / 20.0;
    }
} 