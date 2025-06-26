package com.examcell.resultgen.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByRollNumber(String rollNumber);
    Optional<Student> findByUsername(String username);
    boolean existsByUsername(String username);

    // Add custom query methods if needed, e.g., find by batchYear, course, branch

    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
    Page<Student> findByBranch(Branch branch, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE (LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%'))) AND s.branch = :branch")
    Page<Student> findBySearchAndBranch(@Param("search") String search, @Param("branch") Branch branch, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE (LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Student> findBySearch(@Param("search") String search, Pageable pageable);

    Page<Student> findByBranchAndBatchYear(Branch branch, Integer batchYear, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE " +
           "(:search IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')))" +
           " AND (:branch IS NULL OR s.branch = :branch)" +
           " AND (:course IS NULL OR s.course = :course)")
    Page<Student> findAllStudentsByCriteria(
            @Param("search") String search,
            @Param("branch") Branch branch,
            @Param("course") com.examcell.resultgen.model.Course course,
            Pageable pageable);
} 