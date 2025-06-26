package com.examcell.resultgen.service;

import java.util.List;
import java.util.UUID;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.TeacherDTO;
import com.examcell.resultgen.dto.TeacherDashboardDTO;

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

    /**
     * Get a professor by their ID.
     *
     * @param id The UUID of the professor.
     * @return A TeacherDTO representing the professor.
     */
    TeacherDTO getProfessorById(UUID id);

    /**
     * Get the dashboard summary for a specific professor.
     *
     * @param teacherId The UUID of the professor.
     * @return A TeacherDashboardDTO containing aggregated information.
     */
    TeacherDashboardDTO getTeacherDashboard(UUID teacherId);
} 