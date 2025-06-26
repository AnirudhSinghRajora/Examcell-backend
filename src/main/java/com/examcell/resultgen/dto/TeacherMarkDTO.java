package com.examcell.resultgen.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherMarkDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentRollNo;
    private UUID subjectId;
    private String subjectName;
    private String subjectCode;
    private Double marks; // total
    private String grade;
    private String examType;
    private String academicYear;
    private Double internal1;
    private Double internal2;
    private Double external;
    private Instant createdAt;
    private Instant updatedAt;
} 