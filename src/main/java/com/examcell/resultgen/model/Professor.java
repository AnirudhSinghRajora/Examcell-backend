package com.examcell.resultgen.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Professor extends User {

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = true) // Department can be optional initially or set during onboarding
    private String department;

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

    public Professor(String username, String password, String firstName, String lastName, Role role, String employeeId) {
        super(username, password, firstName, lastName, role);
        this.employeeId = employeeId;
    }

    public Professor(String username, String password, String firstName, String lastName, Role role, String employeeId, String department) {
        super(username, password, firstName, lastName, role);
        this.employeeId = employeeId;
        this.department = department;
    }
} 