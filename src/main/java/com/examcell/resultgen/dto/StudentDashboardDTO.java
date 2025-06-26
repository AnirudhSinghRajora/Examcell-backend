package com.examcell.resultgen.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDashboardDTO {
    // Student information
    private StudentInfo student;
    
    // Dashboard statistics
    private double cgpa;
    private int completedSemesters;
    private int totalSemesters;
    private int pendingQueries;
    private int availableCertificates;
    
    // Recent data
    private List<RecentResultDTO> recentResults;
    private List<RecentQueryDTO> recentQueries;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentInfo {
        private String id;
        private String rollNo;
        private String name;
        private String email;
        private String semester;
        private String department;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentResultDTO {
        private String id;
        private String subjectName;
        private String subjectCode;
        private double marks;
        private String grade;
        private String examType;
        private String academicYear;
        private String createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentQueryDTO {
        private String id;
        private String subject;
        private String faculty;
        private String title;
        private String description;
        private String status;
        private String priority;
        private String response;
        private String respondedBy;
        private String respondedAt;
        private String createdAt;
        private String updatedAt;
    }
} 