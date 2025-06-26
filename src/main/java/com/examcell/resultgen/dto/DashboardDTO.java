package com.examcell.resultgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private long totalStudents;
    private long totalProfessors;
    private long totalSubjects;
    private long totalQueriesOpen;
    private long totalBonafideRequestsPending;
    private long totalMarksRecords;
    // Add more fields as needed for a comprehensive dashboard
} 