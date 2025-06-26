package com.examcell.resultgen.dto;

import java.util.UUID;

import com.examcell.resultgen.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UUID id;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
} 