package com.examcell.resultgen.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.dto.SemesterResultSummaryDto;
import com.examcell.resultgen.dto.StudentDashboardDTO;
import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.YearResultSummaryDto;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.MarksRecordMapper;
import com.examcell.resultgen.mapper.StudentMapper;
import com.examcell.resultgen.mapper.SubjectMapper;
import com.examcell.resultgen.model.BonafideRequest.BonafideRequestStatus;
import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Course;
import com.examcell.resultgen.model.MarksRecord;
import com.examcell.resultgen.model.Query;
import com.examcell.resultgen.model.Query.QueryStatus;
import com.examcell.resultgen.model.Role;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.Subject;
import com.examcell.resultgen.repository.BonafideRequestRepository;
import com.examcell.resultgen.repository.MarksRecordRepository;
import com.examcell.resultgen.repository.QueryRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.repository.UserRepository;
import com.examcell.resultgen.service.StudentService;
import com.examcell.resultgen.util.GradeCalculatorUtil;
import com.examcell.resultgen.util.SemesterUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Injects repositories and mappers via constructor
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRecordRepository marksRecordRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QueryRepository queryRepository; // Inject QueryRepository
    private final BonafideRequestRepository bonafideRequestRepository; // Inject BonafideRequestRepository

    private final StudentMapper studentMapper; // Injected Mapper
    private final SubjectMapper subjectMapper;         // Injected Mapper
    private final MarksRecordMapper marksRecordMapper; // Injected Mapper

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> getAllStudents(String search, Integer semester, String course, String branch, Pageable pageable) {
        Branch branchEnum = (branch != null && !branch.isEmpty() && !branch.equalsIgnoreCase("all")) ? Branch.valueOf(branch.toUpperCase()) : null;
        Course courseEnum = (course != null && !course.isEmpty() && !course.equalsIgnoreCase("all")) ? Course.valueOf(course.toUpperCase()) : null;

        logger.info("Filtering students with: search={}, semester={}, course={}, branch={}",
                search, semester, course, branch);
        logger.info("Converted Enums: branchEnum={}, courseEnum={}", branchEnum, courseEnum);

        Page<Student> studentsPage;
        if (semester != null) {
            // Filter by semester after fetching, as it's not a direct field in Student model
            List<Student> filteredStudents = studentRepository.findAllStudentsByCriteria(search, branchEnum, courseEnum, Pageable.unpaged()).getContent().stream()
                    .filter(student -> SemesterUtil.calculateCurrentSemester(student.getBatchYear()) == semester)
                    .toList();
            
            // Manually paginate the filtered list
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredStudents.size());
            List<Student> paginatedStudents = filteredStudents.subList(start, end);
            studentsPage = new PageImpl<>(paginatedStudents, pageable, filteredStudents.size());

        } else {
            studentsPage = studentRepository.findAllStudentsByCriteria(search, branchEnum, courseEnum, pageable);
        }
        return studentsPage.map(studentMapper::studentToStudentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + id));
        return studentMapper.studentToStudentDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentByRollNumber(String rollNo) {
        Student student = studentRepository.findByRollNumber(rollNo)
                .orElseThrow(() -> new NotFoundException("Student not found with Roll Number: " + rollNo));
        return studentMapper.studentToStudentDto(student);
    }

    @Override
    @Transactional
    public StudentDto createStudent(StudentDto studentDTO) {
        if (userRepository.existsByUsername(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Student with this email already exists: " + studentDTO.getEmail());
        }
        if (studentRepository.findByRollNumber(studentDTO.getRollNumber()).isPresent()) {
            throw new IllegalArgumentException("Student with this Roll Number already exists: " + studentDTO.getRollNumber());
        }

        // In a real application, you'd generate a secure initial password or use an onboarding process
        String encodedPassword = passwordEncoder.encode("defaultPassword");

        Student newStudent = new Student(
                studentDTO.getEmail(),
                encodedPassword,
                studentDTO.getFirstName(),
                studentDTO.getLastName(),
                Role.STUDENT,
                studentDTO.getRollNumber(),
                studentDTO.getCourse(),
                studentDTO.getBranch(),
                studentDTO.getBatchYear()
        );
        Student savedStudent = studentRepository.save(newStudent);
        return studentMapper.studentToStudentDto(savedStudent);
    }

    @Override
    @Transactional
    public StudentDto updateStudent(UUID id, StudentDto studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + id));

        // Check for email uniqueness if changed
        if (!existingStudent.getUsername().equals(studentDTO.getEmail()) && userRepository.existsByUsername(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use by another user: " + studentDTO.getEmail());
        }

        // Check for roll number uniqueness if changed
        if (!existingStudent.getRollNumber().equals(studentDTO.getRollNumber()) && studentRepository.findByRollNumber(studentDTO.getRollNumber()).isPresent()) {
            throw new IllegalArgumentException("Roll Number already in use by another student: " + studentDTO.getRollNumber());
        }

        existingStudent.setUsername(studentDTO.getEmail());
        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setRollNumber(studentDTO.getRollNumber());
        existingStudent.setCourse(studentDTO.getCourse());
        existingStudent.setBranch(studentDTO.getBranch());
        existingStudent.setBatchYear(studentDTO.getBatchYear());
        // Password is not updated via this DTO

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.studentToStudentDto(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(UUID id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDto> getCurrentSubjects(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new NotFoundException("Student not found with Roll Number: " + rollNumber));

        int currentSemester = SemesterUtil.calculateCurrentSemester(student.getBatchYear());
        if (currentSemester == 0) {
            return Collections.emptyList(); // Student hasn't started yet or invalid year
        }

        List<Subject> subjects = subjectRepository.findByCourseAndBranchAndSemester(
                student.getCourse(), student.getBranch(), currentSemester);

        // Use mapper
        return subjectMapper.subjectsToSubjectDtos(subjects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultDto> getAllResults(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new NotFoundException("Student not found with Roll Number: " + rollNumber));

        // Fetch using student ID after finding the student by roll number
        List<MarksRecord> marks = marksRecordRepository.findByStudentId(student.getId());
        // Use mapper
        return marksRecordMapper.marksRecordsToResultDtos(marks);
    }

    @Override
    @Transactional(readOnly = true)
    public SemesterResultSummaryDto getResultsBySemester(String rollNumber, int semester) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new NotFoundException("Student not found with Roll Number: " + rollNumber));

        if (semester <= 0) {
             throw new IllegalArgumentException("Invalid semester number: " + semester);
        }

        // Fetch ALL marks for the student
        List<MarksRecord> allMarksRecords = marksRecordRepository.findByStudentId(student.getId());

        // Filter marks for the requested semester
        List<MarksRecord> semesterMarksRecords = allMarksRecords.stream()
                .filter(mr -> mr.getSubject().getSemester().equals(semester))
                .toList();

        // Calculate SGPA for the current semester using weighted marks
        double semesterWeightedGradePoints = 0.0;
        double semesterTotalCredits = 0.0;
        for (MarksRecord mr : semesterMarksRecords) {
            // Calculate weighted total marks: (internal1 + internal2) * 0.4 + external * 0.6
            double internalAverage = (mr.getInternal1() + mr.getInternal2()) / 2.0;
            double weightedTotalMarks = (internalAverage * 0.4) + (mr.getExternal() * 0.6);
            double gradePoint = GradeCalculatorUtil.calculateGradePoint(weightedTotalMarks);
            double credits = mr.getSubject().getCredits();
            semesterWeightedGradePoints += (gradePoint * credits);
            semesterTotalCredits += credits;
        }
        double sgpa = (semesterTotalCredits > 0) ? (semesterWeightedGradePoints / semesterTotalCredits) : 0.0;
        // Round SGPA to nearest 0.05
        sgpa = GradeCalculatorUtil.roundToNearest005(sgpa);

        // Map semester-specific marks to ResultDto
        List<ResultDto> semesterResultDtos = marksRecordMapper.marksRecordsToResultDtos(semesterMarksRecords);

        return new SemesterResultSummaryDto(semester, semesterResultDtos, sgpa);
    }

    @Override
    @Transactional(readOnly = true)
    public YearResultSummaryDto getResultsByYear(String rollNumber, int yearNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new NotFoundException("Student not found with Roll Number: " + rollNumber));

        // Calculate the two semesters for the given year
        int semester1 = (yearNumber - 1) * 2 + 1;
        int semester2 = semester1 + 1;

        SemesterResultSummaryDto sem1Result = getResultsBySemester(rollNumber, semester1);
        SemesterResultSummaryDto sem2Result = getResultsBySemester(rollNumber, semester2);

        boolean sem1HasMarks = sem1Result.getSubjectResults() != null && !sem1Result.getSubjectResults().isEmpty();
        boolean sem2HasMarks = sem2Result.getSubjectResults() != null && !sem2Result.getSubjectResults().isEmpty();

        Double cgpa = null;
        if (sem1HasMarks && sem2HasMarks) {
            // Combine all subject results from both semesters up to this year
            double totalWeightedGradePoints = 0.0;
            double totalCredits = 0.0;
            for (ResultDto r : sem1Result.getSubjectResults()) {
                totalWeightedGradePoints += r.getGradePoint() * r.getSubject().getCredits();
                totalCredits += r.getSubject().getCredits();
            }
            for (ResultDto r : sem2Result.getSubjectResults()) {
                totalWeightedGradePoints += r.getGradePoint() * r.getSubject().getCredits();
                totalCredits += r.getSubject().getCredits();
            }
            cgpa = (totalCredits > 0) ? (totalWeightedGradePoints / totalCredits) : null;
            if (cgpa != null) {
                cgpa = GradeCalculatorUtil.roundToNearest005(cgpa);
            }
        }

        return new YearResultSummaryDto(
            yearNumber,
            List.of(sem1Result, sem2Result),
            cgpa
        );
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDashboardDTO getStudentDashboard(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));

        // Calculate current semester
        int currentSemester = SemesterUtil.calculateCurrentSemester(student.getBatchYear());
        
        System.out.println("DEBUG: Dashboard - studentId: " + studentId + ", batchYear: " + student.getBatchYear() + ", currentSemester: " + currentSemester);
        
        // Calculate CGPA properly using all marks records
        List<MarksRecord> allMarksRecords = marksRecordRepository.findByStudentId(studentId);
        double cgpa = 0.0;
        if (!allMarksRecords.isEmpty()) {
            double totalWeightedGradePoints = 0.0;
            double totalCredits = 0.0;
            
            for (MarksRecord mr : allMarksRecords) {
                // Calculate weighted total marks: (internal1 + internal2) * 0.4 + external * 0.6
                double internalAverage = (mr.getInternal1() + mr.getInternal2()) / 2.0;
                double weightedTotalMarks = (internalAverage * 0.4) + (mr.getExternal() * 0.6);
                
                // Calculate grade point based on weighted total marks
                double gradePoint = GradeCalculatorUtil.calculateGradePoint(weightedTotalMarks);
                double credits = mr.getSubject().getCredits();
                
                totalWeightedGradePoints += (gradePoint * credits);
                totalCredits += credits;
            }
            cgpa = totalCredits > 0 ? totalWeightedGradePoints / totalCredits : 0.0;
            // Round CGPA to nearest 0.05
            cgpa = GradeCalculatorUtil.roundToNearest005(cgpa);
        }

        // Get recent results (last 5) - using MarksRecord directly for more data
        List<MarksRecord> recentMarksRecords = allMarksRecords.stream()
                .limit(5)
                .collect(Collectors.toList());
        
        List<StudentDashboardDTO.RecentResultDTO> recentResults = recentMarksRecords.stream()
                .map(mr -> {
                    // Calculate weighted total marks: (internal1 + internal2) * 0.4 + external * 0.6
                    double internalAverage = (mr.getInternal1() + mr.getInternal2()) / 2.0;
                    double weightedTotalMarks = (internalAverage * 0.4) + (mr.getExternal() * 0.6);
                    String grade = GradeCalculatorUtil.calculateGrade(weightedTotalMarks);
                    
                    return new StudentDashboardDTO.RecentResultDTO(
                            mr.getId().toString(),
                            mr.getSubject().getName(),
                            mr.getSubject().getCode(),
                            Math.round(weightedTotalMarks * 100.0) / 100.0, // Round to 2 decimal places
                            grade,
                            "FINAL", // examType
                            String.valueOf(student.getBatchYear()), // academicYear
                            mr.getCreatedAt() != null ? mr.getCreatedAt().toString() : "N/A" // createdAt
                    );
                })
                .collect(Collectors.toList());

        // Get recent queries (last 5) - using available method
        List<Query> recentQueries = queryRepository.findByStudentId(studentId)
                .stream()
                .limit(5)
                .collect(Collectors.toList());
        
        List<StudentDashboardDTO.RecentQueryDTO> recentQueriesDTO = recentQueries.stream()
                .map(q -> new StudentDashboardDTO.RecentQueryDTO(
                        q.getId().toString(),
                        q.getSubject(),
                        q.getTeacher() != null ? q.getTeacher().getFirstName() + " " + q.getTeacher().getLastName() : "N/A",
                        q.getSubject(), // Use subject as title since there's no title field
                        q.getQueryText(), // Use queryText as description
                        q.getStatus().toString(),
                        "NORMAL", // Default priority since there's no priority field
                        q.getResponse(),
                        q.getRespondedBy(),
                        q.getRespondedAt() != null ? q.getRespondedAt().toString() : null,
                        q.getCreatedAt().toString(),
                        q.getCreatedAt().toString() // Use createdAt as updatedAt since there's no updatedAt field
                ))
                .collect(Collectors.toList());

        // Count pending queries
        int pendingQueries = (int) queryRepository.countByStudentIdAndStatus(studentId, QueryStatus.OPEN);

        // Count available certificates (bonafide requests that are approved)
        int availableCertificates = (int) bonafideRequestRepository.countByStudentIdAndStatus(studentId, BonafideRequestStatus.APPROVED);

        // Create student info
        StudentDashboardDTO.StudentInfo studentInfo = new StudentDashboardDTO.StudentInfo(
                student.getId().toString(),
                student.getRollNumber(),
                student.getFirstName() + " " + student.getLastName(),
                student.getUsername(),
                String.valueOf(currentSemester),
                student.getBranch().toString()
        );

        System.out.println("DEBUG: Dashboard - returning semester: " + currentSemester + ", completedSemesters: " + (currentSemester - 1));

        return new StudentDashboardDTO(
                studentInfo,
                cgpa, // Already rounded to 0.05 increments
                currentSemester - 1, // completed semesters
                8, // total semesters (assuming 4-year course)
                pendingQueries,
                availableCertificates,
                recentResults,
                recentQueriesDTO
        );
    }
}