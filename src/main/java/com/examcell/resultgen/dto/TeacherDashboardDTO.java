package com.examcell.resultgen.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDashboardDTO {
    private TeacherInfo teacher;
    private int totalStudents;
    private int pendingQueries;
    private int subjectsTeaching;
    private int marksUploaded;
    private List<TeacherQuery> recentQueries;
    private List<TeacherSubject> assignedSubjects;
    private List<RecentMarksUploadDTO> recentMarksUploads;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherInfo {
        private String id;
        private String employeeId;
        private String name;
        private String email;
        private String department;
        private String designation;
        private String specialization;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherQuery {
        private String id;
        private String studentId;
        private String subject;
        private String faculty;
        private String title;
        private String description;
        private String status;
        private String priority;
        private String response;
        private String respondedBy;
        private String respondedAt;
        private String studentName;
        private String studentRollNo;
        private String studentEmail;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherSubject {
        private String id;
        private String code;
        private String name;
        private String semester;
        private String department;
        private int credits;
        private String faculty;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentMarksUploadDTO {
        private String subjectName;
        private String semester;
        private LocalDate uploadDate;
    }
} 