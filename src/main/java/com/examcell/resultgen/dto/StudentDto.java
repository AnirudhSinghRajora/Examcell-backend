package com.examcell.resultgen.dto;

import java.util.UUID;

import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private UUID id;
    private String rollNumber;
    private Course course;
    private Branch branch;
    private Integer batchYear;
} 