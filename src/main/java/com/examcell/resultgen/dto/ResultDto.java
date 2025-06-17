package com.examcell.resultgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private SubjectDto subject;
    private double marksObtained;
    private double maxMarks;
    private Integer semester; // The semester in which these marks were obtained (can differ from subject's default semester)
    private double gradePoint;
} 