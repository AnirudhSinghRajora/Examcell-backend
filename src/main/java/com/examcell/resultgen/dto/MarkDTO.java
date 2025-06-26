package com.examcell.resultgen.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentRollNo;
    private UUID subjectId;
    private String subjectName;
    private String semester; // Can be derived or explicitly stored in DTO for display
    private double internal1;
    private double internal2;
    private double external;
    private double marks; // Calculated total marks
    private String grade; // Calculated grade
} 