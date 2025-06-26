package com.examcell.resultgen.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "queries")
@Data
@NoArgsConstructor
public class Query {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Professor teacher;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String queryText;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(name = "responded_by")
    private String respondedBy;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueryStatus status = QueryStatus.OPEN;

    @CreationTimestamp
    private Instant createdAt;

    public Query(Student student, String subject, String queryText) {
        this.student = student;
        this.subject = subject;
        this.queryText = queryText;
    }

    public Query(Professor teacher, String subject, String queryText) {
        this.teacher = teacher;
        this.subject = subject;
        this.queryText = queryText;
    }

    public enum QueryStatus {
        OPEN, RESOLVED, CLOSED
    }
} 