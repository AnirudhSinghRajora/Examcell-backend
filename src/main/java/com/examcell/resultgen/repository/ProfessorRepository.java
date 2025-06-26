package com.examcell.resultgen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Optional<Professor> findByEmployeeId(String employeeId);

    // Query to check if a professor is assigned a specific subject
    boolean existsByIdAndAssignedSubjectsId(UUID professorId, UUID subjectId);

    // Find professors who teach a specific subject by name
    @Query("SELECT p FROM Professor p JOIN p.assignedSubjects s WHERE s.name = :subjectName")
    List<Professor> findByAssignedSubjectsName(@Param("subjectName") String subjectName);

    Page<Professor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
    Page<Professor> findByDepartmentContainingIgnoreCase(String department, Pageable pageable);
    Page<Professor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndDepartment(String firstName, String lastName, String department, Pageable pageable);
} 