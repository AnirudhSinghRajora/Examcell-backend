package com.examcell.resultgen.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

    @Column(nullable = false, unique = true)
    private String rollNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Integer batchYear; // Year of intake, e.g., 2024

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MarksRecord> marksRecords;

    public Student(String username, String rollNumber, Course course, Branch branch, Integer batchYear) {
        super(username, Role.STUDENT);
        this.rollNumber = rollNumber;
        this.course = course;
        this.branch = branch;
        this.batchYear = batchYear;
    }
} 