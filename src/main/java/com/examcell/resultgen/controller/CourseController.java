package com.examcell.resultgen.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.model.Course;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    public static class CourseInfo {
        public String name;
        public int totalSemesters;
        public CourseInfo(String name, int totalSemesters) {
            this.name = name;
            this.totalSemesters = totalSemesters;
        }
    }

    @GetMapping
    public List<CourseInfo> getCourses() {
        return Arrays.stream(Course.values())
                .map(c -> new CourseInfo(c.name(), c.getTotalSemesters()))
                .collect(Collectors.toList());
    }
} 