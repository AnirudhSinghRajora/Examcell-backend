package com.examcell.resultgen.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Placeholder: Add actual branch names relevant to the courses
public enum Branch {
    // BTECH Branches
    CSE(Course.BTECH),
    ECE(Course.BTECH),
    MECH(Course.BTECH),
    CIVIL(Course.BTECH),
    // MTECH Branches
    AI_ML(Course.MTECH),
    DATA_SCIENCE(Course.MTECH),
    // MSC Branches
    PHYSICS(Course.MSC),
    MATHS(Course.MSC),
    // MBA Branches
    FINANCE(Course.MBA),
    MARKETING(Course.MBA);

    private final Course course;

    Branch(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public static List<Branch> getByCourse(Course course) {
        return Arrays.stream(Branch.values())
                .filter(b -> b.course == course)
                .collect(Collectors.toList());
    }
} 