package com.examcell.resultgen.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Course;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@CrossOrigin
public class BranchController {

    /**
     * GET /api/branches
     * Retrieves a list of all available branch names (from the Branch enum), or by course if provided.
     */
    @GetMapping
    public ResponseEntity<List<String>> getBranchNames(@RequestParam(value = "course", required = false) String course) {
        List<String> branchNames;
        if (course != null) {
            try {
                Course courseEnum = Course.valueOf(course);
                branchNames = Branch.getByCourse(courseEnum).stream().map(Enum::name).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                branchNames = List.of();
            }
        } else {
            branchNames = Arrays.stream(Branch.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(branchNames);
    }
} 