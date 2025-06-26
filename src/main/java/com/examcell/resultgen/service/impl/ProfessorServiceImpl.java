package com.examcell.resultgen.service.impl;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.TeacherDTO;
import com.examcell.resultgen.dto.TeacherDashboardDTO;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.MarksRecordMapper;
import com.examcell.resultgen.mapper.ProfessorMapper;
import com.examcell.resultgen.mapper.StudentMapper;
import com.examcell.resultgen.mapper.SubjectMapper;
import com.examcell.resultgen.model.Enrollment;
import com.examcell.resultgen.model.Professor;
import com.examcell.resultgen.model.Query.QueryStatus;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.Subject;
import com.examcell.resultgen.repository.EnrollmentRepository;
import com.examcell.resultgen.repository.MarksRecordRepository;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.QueryRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final MarksRecordRepository marksRecordRepository;
    private final QueryRepository queryRepository;
    private final StudentRepository studentRepository;

    private final SubjectMapper subjectMapper;
    private final StudentMapper studentMapper;
    private final ProfessorMapper professorMapper;
    private final MarksRecordMapper marksRecordMapper;

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

    @Override
    @Transactional(readOnly = true)
    public TeacherDTO getProfessorById(UUID id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + id));
        return professorMapper.professorToTeacherDTO(professor);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDashboardDTO getTeacherDashboard(UUID teacherId) {
        Professor professor = professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        // Total Subjects Assigned
        List<Subject> assignedSubjects = subjectRepository.findSubjectsByProfessorId(teacherId);
        int totalSubjects = assignedSubjects.size();

        // Total Students - Use a direct query to avoid circular references
        long totalStudents = enrollmentRepository.countDistinctStudentsByProfessorId(teacherId);

        // Pending Queries
        long pendingQueries = queryRepository.countByTeacherIdAndStatus(teacherId, QueryStatus.OPEN);

        // Recent Marks Uploads (last 5, for example)
        List<com.examcell.resultgen.model.MarksRecord> recentMarksRecords = marksRecordRepository.findTop5ByEnteredByIdOrderByCreatedAtDesc(teacherId);
        List<TeacherDashboardDTO.RecentMarksUploadDTO> recentMarksUploads = recentMarksRecords.stream()
                .map(mr -> new TeacherDashboardDTO.RecentMarksUploadDTO(
                        mr.getSubject().getName(),
                        String.valueOf(mr.getSubject().getSemester()),
                        mr.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate()))
                .collect(Collectors.toList());

        // Convert assigned subjects to DTOs
        List<TeacherDashboardDTO.TeacherSubject> assignedSubjectsDTO = assignedSubjects.stream()
                .map(subject -> new TeacherDashboardDTO.TeacherSubject(
                        subject.getId().toString(),
                        subject.getCode(),
                        subject.getName(),
                        String.valueOf(subject.getSemester()),
                        professor.getDepartment() != null ? professor.getDepartment() : "N/A",
                        subject.getCredits(),
                        professor.getFirstName() + " " + professor.getLastName()))
                .collect(Collectors.toList());

        // Get recent queries (last 5)
        List<com.examcell.resultgen.model.Query> recentQueries = queryRepository.findByTeacherId(teacherId);
        List<TeacherDashboardDTO.TeacherQuery> recentQueriesDTO = recentQueries.stream()
                .limit(5)
                .map(query -> new TeacherDashboardDTO.TeacherQuery(
                        query.getId().toString(),
                        query.getStudent().getId().toString(),
                        query.getSubject(),
                        "N/A", // faculty - not in Query entity
                        "Query", // title - not in Query entity, using default
                        query.getQueryText(),
                        query.getStatus().name(),
                        "NORMAL", // priority - not in Query entity, using default
                        query.getResponse(),
                        query.getRespondedBy(), // This is a String, not a Professor
                        query.getRespondedAt() != null ? query.getRespondedAt().toString() : null,
                        query.getStudent().getFirstName() + " " + query.getStudent().getLastName(),
                        query.getStudent().getRollNumber(),
                        query.getStudent().getUsername(), // email not available, using username
                        query.getCreatedAt().toString(),
                        query.getCreatedAt().toString())) // updatedAt not available, using createdAt
                .collect(Collectors.toList());

        // Count marks uploaded by this professor
        long marksUploaded = marksRecordRepository.countByEnteredById(teacherId);

        // Create teacher info
        TeacherDashboardDTO.TeacherInfo teacherInfo = new TeacherDashboardDTO.TeacherInfo(
                professor.getId().toString(),
                professor.getEmployeeId(),
                professor.getFirstName() + " " + professor.getLastName(),
                professor.getUsername(), // email not available, using username
                professor.getDepartment() != null ? professor.getDepartment() : "N/A",
                "Professor", // designation
                "N/A" // specialization
        );

        return new TeacherDashboardDTO(
                teacherInfo,
                (int) totalStudents,
                (int) pendingQueries,
                totalSubjects, // subjectsTeaching
                (int) marksUploaded,
                recentQueriesDTO,
                assignedSubjectsDTO,
                recentMarksUploads
        );
    }
} 