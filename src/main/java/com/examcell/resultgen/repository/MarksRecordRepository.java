package com.examcell.resultgen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.MarksRecord;

@Repository
public interface MarksRecordRepository extends JpaRepository<MarksRecord, UUID> {

    Optional<MarksRecord> findByStudentIdAndSubjectId(UUID studentId, UUID subjectId);
    Page<MarksRecord> findByStudentIdAndSubjectId(UUID studentId, UUID subjectId, Pageable pageable);

    @Query("SELECT mr FROM MarksRecord mr JOIN FETCH mr.student JOIN FETCH mr.subject WHERE mr.student.id = :studentId")
    List<MarksRecord> findByStudentId(@Param("studentId") UUID studentId);
    @EntityGraph(attributePaths = {"student", "subject"})
    Page<MarksRecord> findByStudentId(UUID studentId, Pageable pageable);

    @Query("SELECT mr FROM MarksRecord mr JOIN FETCH mr.student JOIN FETCH mr.subject WHERE mr.student.id = :studentId AND mr.subject.semester = :semester")
    List<MarksRecord> findByStudentIdAndSemester(@Param("studentId") UUID studentId, @Param("semester") int semester);

    @EntityGraph(attributePaths = {"student", "subject"})
    List<MarksRecord> findByEnteredById(UUID professorId);

    @Query("SELECT mr FROM MarksRecord mr JOIN FETCH mr.student JOIN FETCH mr.subject WHERE mr.subject.id = :subjectId")
    List<MarksRecord> findBySubjectId(UUID subjectId);
    @EntityGraph(attributePaths = {"student", "subject"})
    Page<MarksRecord> findBySubjectId(UUID subjectId, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "subject"})
    List<MarksRecord> findByStudentIdInAndSubjectIdIn(List<UUID> studentIds, List<UUID> subjectIds);

    @EntityGraph(attributePaths = {"student", "subject"})
    List<MarksRecord> findTop5ByEnteredByIdOrderByCreatedAtDesc(UUID enteredById);

    @EntityGraph(attributePaths = {"student", "subject"})
    List<MarksRecord> findByEnteredByIdAndSubjectId(UUID enteredById, UUID subjectId);

    long countByEnteredById(UUID enteredById);

    @EntityGraph(attributePaths = {"student", "subject"})
    Page<MarksRecord> findAll(Pageable pageable);
} 