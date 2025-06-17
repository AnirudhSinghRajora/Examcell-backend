package com.examcell.resultgen.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.StudentMapper;
import com.examcell.resultgen.mapper.SubjectMapper;
import com.examcell.resultgen.model.Enrollment;
import com.examcell.resultgen.model.Professor;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.Subject;
import com.examcell.resultgen.repository.EnrollmentRepository;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubjectMapper subjectMapper;
    private final StudentMapper studentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDto> getAssignedSubjects(String employeeId) {
        Professor professor = professorRepository.findByEmployeeId(employeeId)
             .orElseThrow(() -> new NotFoundException("Professor not found with Employee ID: " + employeeId));

        // Find subjects using the professor's internal UUID
        List<Subject> subjects = subjectRepository.findSubjectsByProfessorId(professor.getId());

        return subjectMapper.subjectsToSubjectDtos(subjects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsBySubject(UUID subjectId) {
        // 1. Verify subject exists
        if (!subjectRepository.existsById(subjectId)) {
            throw new NotFoundException("Subject not found with ID: " + subjectId);
        }

        // 2. Authorization Check Removed - Previously checked if professor was assigned.
        // Now we just fetch students for the subject directly.

        // 3. Find enrollments. Ensure Student is fetched eagerly or via join fetch.
        List<Enrollment> enrollments = enrollmentRepository.findBySubjectId(subjectId);

        // 4. Extract students and map to DTOs using mapper
        List<Student> students = enrollments.stream()
                                        .map(Enrollment::getStudent)
                                        .collect(Collectors.toList());
        return studentMapper.studentsToStudentDtos(students);
    }

    // Manual mapping methods are no longer needed
    // private SubjectDto mapToSubjectDto(...) { ... }
    // private StudentDto mapToStudentDto(...) { ... }
} 