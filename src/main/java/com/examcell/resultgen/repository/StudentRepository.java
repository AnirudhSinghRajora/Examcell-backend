package com.examcell.resultgen.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByRollNumber(String rollNumber);
    // Add custom query methods if needed, e.g., find by batchYear, course, branch
} 