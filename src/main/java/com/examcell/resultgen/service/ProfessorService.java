package com.examcell.resultgen.service;

import java.util.List;
import java.util.UUID;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.dto.SubjectDto;

public interface ProfessorService {

    /**
     * Get the list of subjects assigned to a specific professor.
     *
     * @param employeeId The employee ID of the professor.
     * @return A list of SubjectDto assigned to the professor.
     */
    List<SubjectDto> getAssignedSubjects(String employeeId);

    /**
     * Get the list of students enrolled in a specific subject.
     * Note: Authorization (is professor allowed to see this?) is removed for now.
     *
     * @param subjectId The ID of the subject.
     * @return A list of StudentDto enrolled in the subject.
     */
    List<StudentDto> getStudentsBySubject(UUID subjectId);
} 