package com.examcell.resultgen.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Professor extends User {

    @Column(nullable = false, unique = true)
    private String employeeId;

    // Subjects assigned to the professor for teaching/marking
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "professor_subject_assignments",
        joinColumns = @JoinColumn(name = "professor_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> assignedSubjects = new HashSet<>();

    public Professor(String username, String employeeId) {
        super(username, Role.PROFESSOR);
        this.employeeId = employeeId;
    }
} 