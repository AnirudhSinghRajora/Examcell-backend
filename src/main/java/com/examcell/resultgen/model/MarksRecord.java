package com.examcell.resultgen.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "marks_records", uniqueConstraints = {
    // A student should have only one mark record per subject
    @UniqueConstraint(columnNames = {"student_id", "subject_id"})
})
@Data
@NoArgsConstructor
public class MarksRecord {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Double internal1;

    @Column(nullable = false)
    private Double internal2;

    @Column(nullable = false)
    private Double external;

    @Column(nullable = false)
    private Double maxMarks = 100.0; // Default max marks, can be overridden

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entered_by_professor_id", nullable = false)
    private Professor enteredBy;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public MarksRecord(Student student, Subject subject, Double internal1, Double internal2, Double external, Professor enteredBy) {
        this.student = student;
        this.subject = subject;
        this.internal1 = internal1;
        this.internal2 = internal2;
        this.external = external;
        this.enteredBy = enteredBy;
    }
} 