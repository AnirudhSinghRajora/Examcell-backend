package com.examcell.resultgen.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentRollNo;
    private String studentEmail;
    private UUID teacherId;
    private String teacherName;
    private String subject;
    private String faculty;
    private String title;
    private String description;
    private String queryText;
    private String response;
    private String respondedBy;
    private Instant respondedAt;
    private String status;
    private String priority;
    private Instant createdAt;
    private Instant updatedAt;
} 