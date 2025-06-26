package com.examcell.resultgen.util;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.stereotype.Component;

@Component
public class SemesterUtil {

    /**
     * Calculates the current semester for a student based on their batch year.
     * Academic calendar:
     * - Odd semesters (1, 3, 5, 7) run roughly July/Aug - Nov/Dec.
     * - Even semesters (2, 4, 6, 8) run roughly Jan/Feb - May/Jun.
     *
     * Example for batch 2021:
     * - Sem 1: Aug-Dec 2021
     * - Sem 2: Jan-May 2022
     * - Sem 3: Aug-Dec 2022
     * - Sem 4: Jan-May 2023
     * - Sem 5: Aug-Dec 2023
     * - Sem 6: Jan-May 2024
     * - Sem 7: Aug-Dec 2024
     * - Sem 8: Jan-May 2025 (Final semester)
     *
     * @param batchYear The year the student joined the course (e.g., 2021).
     * @param currentDate The date for which to calculate the semester.
     * @return The calculated current semester number (1-8 for a typical 4-year course).
     *         Returns 0 if calculation is invalid (e.g., date before intake).
     */
    public static int calculateCurrentSemester(int batchYear, LocalDate currentDate) {
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        // Check if the current date is before the typical start of the first semester
        if (currentYear < batchYear || (currentYear == batchYear && currentMonth.getValue() < Month.AUGUST.getValue())) {
            return 0; // Not started yet or invalid date
        }

        // Calculate years since batch start
        int yearsSinceBatch = currentYear - batchYear;
        
        // Determine if it's the first half (Jan-June) or second half (July-Dec) of the year
        boolean isSecondHalfOfYear = currentMonth.getValue() >= Month.AUGUST.getValue();

        int semester;
        if (isSecondHalfOfYear) {
            // July-Dec: Odd semesters (1, 3, 5, 7)
            semester = (yearsSinceBatch * 2) + 1;
        } else {
            // Jan-June: Even semesters (2, 4, 6, 8)
            semester = (yearsSinceBatch * 2);
        }

        // Cap semester based on typical course duration (8 for B.Tech)
        int maxSemesters = 8;
        return Math.min(semester, maxSemesters);
    }

    // Overload for convenience, using the current system date
    public static int calculateCurrentSemester(int batchYear) {
        return calculateCurrentSemester(batchYear, LocalDate.now());
    }

    public static String getAcademicYearForSemester(int batchYear, int semester) {
        // Semester 1: Aug-Dec batchYear
        // Semester 2: Jan-May batchYear+1
        // Semester 3: Aug-Dec batchYear+1
        // Semester 4: Jan-May batchYear+2
        // Semester 5: Aug-Dec batchYear+2
        // Semester 6: Jan-May batchYear+3
        // Semester 7: Aug-Dec batchYear+3
        // Semester 8: Jan-May batchYear+4
        int yearOffset = (semester - 1) / 2;
        int startYear = batchYear + yearOffset;
        int endYear = (semester % 2 == 0) ? startYear : startYear + 1;
        if (semester % 2 == 0) {
            // Even semester: Jan-May, e.g., 2022-23
            return (startYear - 1) + "-" + String.valueOf(startYear).substring(2);
        } else {
            // Odd semester: Aug-Dec, e.g., 2022-23
            return startYear + "-" + String.valueOf(endYear).substring(2);
        }
    }
} 