package com.examcell.resultgen.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.examcell.resultgen.dto.MarkDTO;
import com.examcell.resultgen.dto.MarkEntryDto;
import com.examcell.resultgen.dto.TeacherMarkDTO;
import com.examcell.resultgen.model.MarksRecord;

public interface MarksService {

    /**
     * Enters marks for a batch of students for specific subjects.
     * Authorization based on professor is removed.
     *
     * @param professorEmployeeId The Employee ID of the professor entering the marks (for tracking).
     * @param markEntries A list of MarkEntryDto objects.
     * @return The list of created or updated MarksRecord entities.
     */
    List<MarksRecord> batchEnterMarks(String professorEmployeeId, List<MarkEntryDto> markEntries);

    /**
     * Updates an existing marks record.
     * Authorization based on professor is removed.
     *
     * @param professorEmployeeId The Employee ID of the professor updating the mark (for tracking).
     * @param recordId The ID of the MarksRecord to update.
     * @param marks The new total marks value.
     * @return The updated MarksRecord entity.
     */
    MarksRecord updateMark(String professorEmployeeId, UUID recordId, Double marks);

    // New methods for ProfessorPortalController
    List<MarkDTO> getMarksByTeacherAndSubject(UUID teacherId, UUID subjectId);
    List<TeacherMarkDTO> getTeacherMarksByTeacherAndSubject(UUID teacherId, UUID subjectId);
    MarkDTO createMark(UUID teacherId, MarkEntryDto markEntryDto);
    MarkDTO updateMarkByTeacher(UUID teacherId, UUID markId, Double internal1, Double internal2, Double external);
    void deleteMarkByTeacher(UUID teacherId, UUID markId);
    List<MarkDTO> uploadMarksFromExcel(UUID teacherId, MultipartFile file, UUID subjectId, String semester);

    // Admin/Centralized marks methods
    Page<MarkDTO> getAllMarksByCriteria(UUID studentId, UUID subjectId, Pageable pageable);
    MarkDTO getMarkById(UUID markId);
    Page<MarkDTO> getMarksByStudentId(UUID studentId, Pageable pageable);
    MarkDTO updateMarkByAdmin(String adminEmployeeId, UUID markId, Double internal1, Double internal2, Double external);
    void deleteMarkByAdmin(UUID markId);
    List<MarkDTO> uploadMarksFromExcel(String adminEmployeeId, MultipartFile file, UUID subjectId, String semester);
} 