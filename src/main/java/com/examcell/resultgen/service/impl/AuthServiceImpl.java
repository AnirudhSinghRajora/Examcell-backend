package com.examcell.resultgen.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.AuthRequest;
import com.examcell.resultgen.dto.AuthResponse;
import com.examcell.resultgen.dto.SignupRequest;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.model.Professor;
import com.examcell.resultgen.model.Role;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.User;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.UserRepository;
import com.examcell.resultgen.security.JwtService;
import com.examcell.resultgen.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists: " + signupRequest.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        User newUser;

        if (signupRequest.getRole() == Role.STUDENT) {
            // Placeholder values for rollNumber, course, branch, batchYear - these would ideally come from signupRequest or be generated
            newUser = new Student(signupRequest.getEmail(), encodedPassword, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getRole(), generateUniqueRollNumber(), null, null, 2024); // TODO: Adjust based on real student registration needs
        } else if (signupRequest.getRole() == Role.PROFESSOR) {
            // Placeholder for employeeId
            newUser = new Professor(signupRequest.getEmail(), encodedPassword, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getRole(), generateUniqueEmployeeId()); // TODO: Adjust based on real professor registration needs
        } else {
            throw new IllegalArgumentException("Invalid role specified: " + signupRequest.getRole());
        }

        User savedUser = userRepository.save(newUser);

        // Generate token for the newly registered user
        // The `generateToken` method in JwtService expects UserDetails. We can create a simple UserDetails object.
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                savedUser.getUsername(),
                savedUser.getPassword(),
                savedUser.getAuthorities() // Assuming User model implements GrantedAuthority or has a method
        );
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(), // Email is the username
                savedUser.getRole(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        );
    }

    @Override
    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
            // If authentication is successful, get user details and generate token
            User user = userRepository.findByUsername(authRequest.getEmail())
                    .orElseThrow(() -> new NotFoundException("User not found: " + authRequest.getEmail()));
            
            // Generate token for the authenticated user
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
            String token = jwtService.generateToken(userDetails);

            return new AuthResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getRole(),
                    user.getFirstName(),
                    user.getLastName()
            );
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    // --- Helper methods for placeholder data ---
    private String generateUniqueRollNumber() {
        return "STU" + System.currentTimeMillis(); // Simple placeholder
    }

    private String generateUniqueEmployeeId() {
        return "EMP" + System.currentTimeMillis(); // Simple placeholder
    }
} 