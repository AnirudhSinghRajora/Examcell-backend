package com.examcell.resultgen.service;

import java.util.List;

import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.dto.SemesterResultSummaryDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.YearResultSummaryDto;

public interface StudentService {

    /**
     * Get the subjects for the student's current semester based on their roll number.
     *
     * @param rollNumber The roll number of the student.
     * @return A list of SubjectDto for the current semester.
     */
    List<SubjectDto> getCurrentSubjects(String rollNumber);

    /**
     * Get all results (marks) for a specific student.
     *
     * @param rollNumber The roll number of the student.
     * @return A list of ResultDto.
     */
    List<ResultDto> getAllResults(String rollNumber);

    /**
     * Get results (marks) for a specific student for a specific semester.
     *
     * @param rollNumber The roll number of the student.
     * @param semester The semester number.
     * @return A list of ResultDto for the specified semester.
     */
    SemesterResultSummaryDto getResultsBySemester(String rollNumber, int semester);

    YearResultSummaryDto getResultsByYear(String rollNumber, int yearNumber);
} 