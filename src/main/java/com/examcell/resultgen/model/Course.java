package com.examcell.resultgen.model;

public enum Course {
    BTECH(8),
    MTECH(4),
    MSC(4),
    MBA(4);

    private final int totalSemesters;

    Course(int totalSemesters) {
        this.totalSemesters = totalSemesters;
    }

    public int getTotalSemesters() {
        return totalSemesters;
    }
} 