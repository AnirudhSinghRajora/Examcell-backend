package com.examcell.resultgen.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bonafide_requests")
@Data
@NoArgsConstructor
public class BonafideRequest {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String purpose;

    @Column(columnDefinition = "TEXT")
    private String customPurpose;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BonafideRequestStatus status = BonafideRequestStatus.PENDING;

    @CreationTimestamp
    private Instant createdAt;

    private Instant approvedAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private String certificateNumber;

    @Column(columnDefinition = "TEXT")
    private String certificatePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_admin_id")
    private User approvedBy;

    @UpdateTimestamp
    private Instant updatedAt;

    public BonafideRequest(Student student, String purpose) {
        this.student = student;
        this.purpose = purpose;
        this.status = BonafideRequestStatus.PENDING;
    }

    public enum BonafideRequestStatus {
        PENDING, APPROVED, REJECTED
    }
} 