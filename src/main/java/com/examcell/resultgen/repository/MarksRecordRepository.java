package com.examcell.resultgen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.MarksRecord;

@Repository
public interface MarksRecordRepository extends JpaRepository<MarksRecord, UUID> {

    Optional<MarksRecord> findByStudentIdAndSubjectId(UUID studentId, UUID subjectId);

    @Query("SELECT mr FROM MarksRecord mr JOIN FETCH mr.subject WHERE mr.student.id = :studentId")
    List<MarksRecord> findByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT mr FROM MarksRecord mr JOIN FETCH mr.subject s WHERE mr.student.id = :studentId AND s.semester = :semester")
    List<MarksRecord> findByStudentIdAndSemester(@Param("studentId") UUID studentId, @Param("semester") int semester);

    List<MarksRecord> findByEnteredById(UUID professorId);

    List<MarksRecord> findBySubjectId(UUID subjectId);

    List<MarksRecord> findByStudentIdInAndSubjectIdIn(List<UUID> studentIds, List<UUID> subjectIds);
} 