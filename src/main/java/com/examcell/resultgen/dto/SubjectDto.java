package com.examcell.resultgen.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private int semester;
    private int credits;
    private String courseName;
    private List<String> branchNames;
    // Note: Course and Branch info might be added if needed client-side
} 