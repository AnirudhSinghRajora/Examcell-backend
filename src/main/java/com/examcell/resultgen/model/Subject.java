package com.examcell.resultgen.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code; // e.g., CS301

    @Column(nullable = false)
    private String name; // e.g., Data Structures

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Course course; // The course this subject belongs to

    // Branches applicable for this subject within the course
    @ElementCollection(targetClass = Branch.class, fetch = FetchType.EAGER) // Eager fetch for simplicity
    @CollectionTable(name = "subject_branches", joinColumns = @JoinColumn(name = "subject_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "branch", nullable = false)
    private Set<Branch> branches = new HashSet<>();

    @Column(nullable = false)
    private Integer semester; // Semester in which this subject is typically taught

    @Column(nullable = false)
    private double credits; // Number of credits for this subject

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MarksRecord> marksRecords;

    // ManyToMany with Professor is defined in Professor class

    public Subject(String code, String name, Course course, Set<Branch> branches, Integer semester) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.branches = branches;
        this.semester = semester;
        this.credits = 0.0; // Default or handle based on actual credits
    }

    public Subject(String code, String name, Course course, Set<Branch> branches, Integer semester, double credits) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.branches = branches;
        this.semester = semester;
        this.credits = credits;
    }
} 