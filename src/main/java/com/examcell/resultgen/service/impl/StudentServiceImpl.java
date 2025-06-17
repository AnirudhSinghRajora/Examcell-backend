package com.examcell.resultgen.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.dto.SemesterResultSummaryDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.YearResultSummaryDto;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.MarksRecordMapper;
import com.examcell.resultgen.mapper.SubjectMapper;
import com.examcell.resultgen.model.MarksRecord;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.Subject;
import com.examcell.resultgen.repository.MarksRecordRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.service.StudentService;
import com.examcell.resultgen.util.GradeCalculatorUtil;
import com.examcell.resultgen.util.SemesterUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Injects repositories and mappers via constructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRecordRepository marksRecordRepository;
    private final SubjectMapper subjectMapper;         // Injected Mapper
    private final MarksRecordMapper marksRecordMapper; // Injected Mapper

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

        // Calculate SGPA for the current semester
        double semesterWeightedGradePoints = 0.0;
        double semesterTotalCredits = 0.0;
        for (MarksRecord mr : semesterMarksRecords) {
            double gradePoint = GradeCalculatorUtil.calculateGradePoint(mr.getMarksObtained());
            double credits = mr.getSubject().getCredits();
            semesterWeightedGradePoints += (gradePoint * credits);
            semesterTotalCredits += credits;
        }
        double sgpa = (semesterTotalCredits > 0) ? (semesterWeightedGradePoints / semesterTotalCredits) : 0.0;

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
        }

        return new YearResultSummaryDto(
            yearNumber,
            List.of(sem1Result, sem2Result),
            cgpa
        );
    }

    // Manual mapping methods are no longer needed
    // private SubjectDto mapToSubjectDto(com.examcell.resultgen.model.Subject subject) { ... }
    // private ResultDto mapToResultDto(com.examcell.resultgen.model.MarksRecord mark) { ... }
} 