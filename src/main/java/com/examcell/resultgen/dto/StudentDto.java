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
    private String firstName;
    private String lastName;
    private String rollNumber;
    private Integer semester;
    private String department;
    private String email;
    private Course course;
    private Branch branch;
    private Integer batchYear;
} 