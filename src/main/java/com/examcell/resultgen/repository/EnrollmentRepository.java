package com.examcell.resultgen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    Optional<Enrollment> findByStudentIdAndSubjectId(UUID studentId, UUID subjectId);

    List<Enrollment> findByStudentId(UUID studentId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.subject.id = :subjectId")
    List<Enrollment> findBySubjectId(@Param("subjectId") UUID subjectId);

    // Check if a student is enrolled in a specific subject
    boolean existsByStudentIdAndSubjectId(UUID studentId, UUID subjectId);

    // Count distinct students enrolled in subjects taught by a professor
    @Query(value = "SELECT COUNT(DISTINCT e.student_id) FROM enrollments e " +
           "JOIN professor_subject_assignments psa ON e.subject_id = psa.subject_id " +
           "WHERE psa.professor_id = :professorId", nativeQuery = true)
    long countDistinctStudentsByProfessorId(@Param("professorId") UUID professorId);
} 