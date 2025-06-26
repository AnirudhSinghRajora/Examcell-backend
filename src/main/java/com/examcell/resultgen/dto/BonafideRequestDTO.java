package com.examcell.resultgen.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonafideRequestDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentRollNo;
    private String studentSemester;
    private String purpose;
    private String customPurpose;
    private String additionalInfo;
    private String status;
    private Instant createdAt;
    private Instant approvedAt;
    private String rejectionReason;
    private String certificateNumber;
    private String certificatePath;
    private String approvedBy;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitRequest {
        private String purpose;
        private String customPurpose;
        private String additionalInfo;
    }
} 