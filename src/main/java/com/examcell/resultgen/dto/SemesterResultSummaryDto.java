package com.examcell.resultgen.dto;

import java.util.List;

public class SemesterResultSummaryDto {
    private int semesterNumber;
    private List<ResultDto> subjectResults;
    private double sgpa;

    public SemesterResultSummaryDto() {}

    public SemesterResultSummaryDto(int semesterNumber, List<ResultDto> subjectResults, double sgpa) {
        this.semesterNumber = semesterNumber;
        this.subjectResults = subjectResults;
        this.sgpa = sgpa;
    }

    public int getSemesterNumber() { return semesterNumber; }
    public void setSemesterNumber(int semesterNumber) { this.semesterNumber = semesterNumber; }
    public List<ResultDto> getSubjectResults() { return subjectResults; }
    public void setSubjectResults(List<ResultDto> subjectResults) { this.subjectResults = subjectResults; }
    public double getSgpa() { return sgpa; }
    public void setSgpa(double sgpa) { this.sgpa = sgpa; }
} 