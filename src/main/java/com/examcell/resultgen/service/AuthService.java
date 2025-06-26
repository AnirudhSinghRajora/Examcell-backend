package com.examcell.resultgen.service;

import com.examcell.resultgen.dto.AuthRequest;
import com.examcell.resultgen.dto.AuthResponse;
import com.examcell.resultgen.dto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest signupRequest);
    AuthResponse login(AuthRequest authRequest);
} 