package com.examcell.resultgen.dto;

import java.util.List;

public class YearResultSummaryDto {
    private int yearNumber;
    private List<SemesterResultSummaryDto> semesterResults;
    private Double cgpa; // Nullable: only present if both semesters have marks

    public YearResultSummaryDto() {}

    public YearResultSummaryDto(int yearNumber, List<SemesterResultSummaryDto> semesterResults, Double cgpa) {
        this.yearNumber = yearNumber;
        this.semesterResults = semesterResults;
        this.cgpa = cgpa;
    }

    public int getYearNumber() { return yearNumber; }
    public void setYearNumber(int yearNumber) { this.yearNumber = yearNumber; }
    public List<SemesterResultSummaryDto> getSemesterResults() { return semesterResults; }
    public void setSemesterResults(List<SemesterResultSummaryDto> semesterResults) { this.semesterResults = semesterResults; }
    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }
} 