package com.examcell.resultgen.util;

import java.time.LocalDate;
import java.time.Month;

public class SemesterUtil {

    /**
     * Calculates the current semester for a student based on their batch year.
     * Assumes standard academic calendars where:
     * - Odd semesters (1, 3, 5, 7) run roughly July/Aug - Nov/Dec.
     * - Even semesters (2, 4, 6, 8) run roughly Jan/Feb - May/Jun.
     *
     * Example:
     * - Batch 2024 intake (usually starts July/Aug 2024) -> In Sem 1 during Aug-Dec 2024.
     * - Batch 2024 intake -> In Sem 2 during Jan-Jun 2025.
     * - Batch 2024 intake -> In Sem 3 during Aug-Dec 2025.
     *
     * @param batchYear The year the student joined the course (e.g., 2024).
     * @param currentDate The date for which to calculate the semester.
     * @return The calculated current semester number (1-8 for a typical 4-year course).
     *         Returns 0 or throws exception if calculation is invalid (e.g., date before intake).
     */
    public static int calculateCurrentSemester(int batchYear, LocalDate currentDate) {
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        // Check if the current date is before the typical start of the first semester
        if (currentYear < batchYear || (currentYear == batchYear && currentMonth.getValue() < Month.JULY.getValue())) {
             // Or throw an IllegalArgumentException("Current date cannot be before the batch intake year/month.");
            return 0; // Not started yet or invalid date
        }

        int yearsPassed = currentYear - batchYear;
        int semester;

        // Determine if it's the first half (Jan-June) or second half (July-Dec) of the year
        boolean isSecondHalfOfYear = currentMonth.getValue() >= Month.JULY.getValue();

        if (isSecondHalfOfYear) {
            // July-Dec: Odd semesters (1, 3, 5, 7)
            semester = (yearsPassed * 2) + 1;
        } else {
            // Jan-June: Even semesters (2, 4, 6, 8)
            // Note: yearsPassed is calculated from the start of the batch year.
            // For Jan-June of the *next* year (yearsPassed=1), it's Semester 2.
            // For Jan-June of year after next (yearsPassed=2), it's Semester 4.
            semester = (yearsPassed * 2);
        }

        // Cap semester based on typical course duration (e.g., 8 for B.Tech)
        // TODO: Make this dependent on the specific course duration if needed
        int maxSemesters = 8;
        return Math.min(semester, maxSemesters);
    }

    // Overload for convenience, using the current system date
    public static int calculateCurrentSemester(int batchYear) {
        return calculateCurrentSemester(batchYear, LocalDate.now());
    }
} 