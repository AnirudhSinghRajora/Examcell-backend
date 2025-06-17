package com.examcell.resultgen.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private UUID id;
    private String code;
    private String name;
    private Integer semester;
    private double credits;
    // Note: Course and Branch info might be added if needed client-side
} 