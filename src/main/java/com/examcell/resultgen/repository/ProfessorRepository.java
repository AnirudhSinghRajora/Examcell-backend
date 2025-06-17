package com.examcell.resultgen.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Optional<Professor> findByEmployeeId(String employeeId);

    // Query to check if a professor is assigned a specific subject
    boolean existsByIdAndAssignedSubjectsId(UUID professorId, UUID subjectId);
} 