package com.examcell.resultgen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Course;
import com.examcell.resultgen.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    Optional<Subject> findByCode(String code);

    // Find subjects relevant for a specific student based on their course, branch, and calculated semester
    @Query("SELECT s FROM Subject s JOIN s.branches b WHERE s.course = :course AND b = :branch AND s.semester = :semester")
    List<Subject> findByCourseAndBranchAndSemester(@Param("course") Course course, @Param("branch") Branch branch, @Param("semester") int semester);

    // Find subjects by course and semester (useful for general lookups)
    List<Subject> findByCourseAndSemester(Course course, int semester);

    // Find subjects assigned to a specific professor using an explicit query
    // This query joins Professor (p) with its assignedSubjects collection (s) 
    // and filters by the professor's ID.
    @Query("SELECT s FROM Professor p JOIN p.assignedSubjects s WHERE p.id = :professorId")
    List<Subject> findSubjectsByProfessorId(@Param("professorId") UUID professorId);

    // Find subjects by ID list
    List<Subject> findByIdIn(List<UUID> ids);

} 