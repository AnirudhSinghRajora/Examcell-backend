package com.examcell.resultgen.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String employeeId;
    private String department;
    private String email;
} 